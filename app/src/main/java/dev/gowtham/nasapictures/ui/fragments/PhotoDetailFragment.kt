package dev.gowtham.nasapictures.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.clear
import coil.loadAny
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.size.OriginalSize
import dev.gowtham.nasapictures.ARG_POSITION
import dev.gowtham.nasapictures.NASAPicturesApp
import dev.gowtham.nasapictures.databinding.PhotoDetailFragmentBinding
import dev.gowtham.nasapictures.util.InjectorUtils
import dev.gowtham.nasapictures.viewmodel.PhotoDetailViewModel
import me.zhanghai.android.systemuihelper.SystemUiHelper
import timber.log.Timber

class PhotoDetailFragment : Fragment() {

    private var position: Int = -1
    private lateinit var systemUiHelper: SystemUiHelper
    private lateinit var viewModel: PhotoDetailViewModel
    private lateinit var binding: PhotoDetailFragmentBinding

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

        binding.photoImageView.setOnClickListener {
            systemUiHelper.toggle()
        }

        val factory =
            InjectorUtils.providePhotoDetailViewModelFactory(requireActivity().application as NASAPicturesApp)
        viewModel = ViewModelProvider(this, factory).get(PhotoDetailViewModel::class.java)
        viewModel.fetchPhoto(position)

        viewModel.currentPhotoModel.observe(viewLifecycleOwner) {
            requireActivity().title = it.title
            binding.photoModel = it
            binding.photoImageView.apply {
                loadAny(it.url) {
                    size(OriginalSize)
                    placeholder(android.R.color.transparent)
//                    TODO: error handling
                    listener(
                        onSuccess = { request: ImageRequest, metadata: ImageResult.Metadata ->
                            Timber.d("image loaded via url - ${metadata.dataSource.name} - ${request.data}")
                        },
                        onError = { _, _ -> }
                    )
                }
            }
        }

        viewModel.largePhotoModel.observe(viewLifecycleOwner) {
            // TODO: animate and fade out old photoImageView
            binding.photoImageView.clear()
            binding.photoImageView.setImageBitmap(it)
        }
    }
}
