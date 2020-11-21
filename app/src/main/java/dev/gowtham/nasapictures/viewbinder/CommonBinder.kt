package dev.gowtham.nasapictures.viewbinder

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.transform.RoundedCornersTransformation
import dev.gowtham.nasapictures.R
import dev.gowtham.nasapictures.util.BindableAdapter
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
@BindingAdapter("data")
fun <T> bindDataToRecyclerView(recyclerView: RecyclerView, data: T) {
    if (data != null && recyclerView.adapter is BindableAdapter<*>) {
        (recyclerView.adapter as BindableAdapter<T>).setData(data)
    }
}

@BindingAdapter("imageUrl")
fun bindImageToImageView(imageView: AppCompatImageView, imageUrl: String?) {
    val transformations = listOf(RoundedCornersTransformation(15F))
    imageUrl?.let {
        val request = ImageRequest.Builder(imageView.context)
            .data(imageUrl)
            .crossfade(true)
            .error(R.drawable.ic_launcher_background) // TODO: replace with proper placeholder
            .fallback(R.drawable.ic_launcher_background)
            .transformations(transformations)
            .target(imageView)
            .listener(object : ImageRequest.Listener {
                override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
                    super.onSuccess(request, metadata)
                    Timber.d("image loaded via ${metadata.dataSource.name} - ${request.data}")
                }
            })
            .build()
        imageView.context.imageLoader.enqueue(request)
    }
}

@BindingAdapter("copyright")
fun bindCopyright(textView: TextView, copyright: String?) {
    if (copyright == null) {
        textView.text = textView.context.getString(R.string.not_available)
    } else {
        textView.text = textView.context.getString(R.string.copyright_text, copyright)
    }
}
