package dev.gowtham.nasapictures.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.request.ImageRequest
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dev.gowtham.nasapictures.ARG_POSITION
import dev.gowtham.nasapictures.NASAPicturesApp
import dev.gowtham.nasapictures.databinding.PhotoDetailFragmentBinding
import dev.gowtham.nasapictures.extensions.bitmap
import dev.gowtham.nasapictures.util.InjectorUtils
import dev.gowtham.nasapictures.viewmodel.PhotoDetailViewModel
import kotlinx.coroutines.*
import me.zhanghai.android.systemuihelper.SystemUiHelper
import timber.log.Timber

class PhotoDetailFragment : Fragment() {

    private var position: Int = -1
    private lateinit var systemUiHelper: SystemUiHelper
    private lateinit var viewModel: PhotoDetailViewModel
    private lateinit var binding: PhotoDetailFragmentBinding
    private val bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
        get() = BottomSheetBehavior.from(binding.bottomSheet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().takeIf { it.containsKey(ARG_POSITION) }?.apply {
            position = getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        PhotoDetailFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // setup window flags
        systemUiHelper = SystemUiHelper(
            activity, SystemUiHelper.LEVEL_IMMERSIVE, SystemUiHelper.FLAG_IMMERSIVE_STICKY
        ) { visible: Boolean ->
            // TODO: animate toolbars
            if (visible) {
                // hide elements
                (requireActivity() as AppCompatActivity).supportActionBar?.show()
                binding.bottomSheet.visibility = View.VISIBLE
            } else {
                // show elements
                (requireActivity() as AppCompatActivity).supportActionBar?.hide()
                binding.bottomSheet.visibility = View.GONE
            }
        }
        systemUiHelper.show()

        binding.imageView.apply {
            setDoubleTapZoomDuration(300)
            orientation = SubsamplingScaleImageView.ORIENTATION_USE_EXIF
            setOnClickListener {
                systemUiHelper.toggle()
            }
        }

        binding.bottomSheet.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        val factory =
            InjectorUtils.providePhotoDetailViewModelFactory(requireActivity().application as NASAPicturesApp)
        viewModel = ViewModelProvider(this, factory).get(PhotoDetailViewModel::class.java)
        viewModel.fetchPhoto(position)

        viewModel.currentPhotoModel.observe(viewLifecycleOwner) {
            requireActivity().title = it.title
            binding.photoModel = it
            lifecycleScope.launch(Dispatchers.IO) {
                val imageRequest = async {
                    val request = ImageRequest.Builder(binding.imageView.context)
                        .data(it.url)
                        .build()
                    binding.imageView.context.imageLoader.execute(request)
                }

                val hdImageRequest = async {
                    val request = ImageRequest.Builder(binding.imageView.context)
                        .data(it.hdUrl)
                        .build()
                    binding.imageView.context.imageLoader.execute(request)
                }
                hdImageRequest.invokeOnCompletion { imageRequest.cancel() }

                try {
                    val imageResult = imageRequest.await()
                    imageResult.drawable?.let { drawable ->
                        withContext(Dispatchers.Main) {
                            binding.imageView.setImage(ImageSource.cachedBitmap(drawable.bitmap()))
                        }
                    }
                } catch (e: CancellationException) {
                    Timber.v("cancelled because high quality image is available")
                }

                val hdImageResult = hdImageRequest.await()
                hdImageResult.drawable?.let { drawable ->
                    withContext(Dispatchers.Main) {
                        binding.imageView.setImage(ImageSource.bitmap(drawable.bitmap()))
                    }
                }
            }
        }
    }
}
