package dev.gowtham.nasapictures.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.gowtham.nasapictures.R
import dev.gowtham.nasapictures.databinding.PhotoItemBinding
import dev.gowtham.nasapictures.model.PhotoModel
import dev.gowtham.nasapictures.util.BindableAdapter

class PhotoAdapter(private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>(), BindableAdapter<List<PhotoModel>> {

    var photoList: MutableList<PhotoModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        return holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size

    override fun setData(data: List<PhotoModel>) {
        if (photoList.isEmpty()) {
            photoList = data.toMutableList()
            notifyItemRangeInserted(0, data.size)
        }
    }

    class PhotoViewHolder(
        private val binding: PhotoItemBinding,
        private val onItemClick: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick.invoke(layoutPosition)
            }
        }

        fun bind(photo: PhotoModel) {
            binding.photo = photo
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup, onItemClick: (Int) -> Unit): PhotoViewHolder {
                val binding: PhotoItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.photo_item, parent, false
                )
                return PhotoViewHolder(binding, onItemClick)
            }
        }
    }
}
