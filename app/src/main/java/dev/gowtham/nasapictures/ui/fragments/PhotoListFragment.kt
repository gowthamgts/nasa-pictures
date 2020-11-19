package dev.gowtham.nasapictures.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.gowtham.nasapictures.NASAPicturesApp
import dev.gowtham.nasapictures.R
import dev.gowtham.nasapictures.adapter.PhotoAdapter
import dev.gowtham.nasapictures.databinding.PhotoListFragmentBinding
import dev.gowtham.nasapictures.util.InjectorUtils
import dev.gowtham.nasapictures.viewmodel.PhotoListViewModel

class PhotoListFragment : Fragment() {

    private lateinit var viewModel: PhotoListViewModel
    private lateinit var binding: PhotoListFragmentBinding
    private val photoListRecycler: RecyclerView
        get() = binding.photoListRecycler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.photo_list_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory =
            InjectorUtils.providePhotoListViewModelFactory(requireActivity().application as NASAPicturesApp)
        viewModel = ViewModelProvider(this, factory).get(PhotoListViewModel::class.java)
        viewModel.fetchPhotoList()

        photoListRecycler.layoutManager = LinearLayoutManager(requireContext())
        photoListRecycler.adapter = PhotoAdapter { clickedPosition ->
            // navigate to photo slider screen
            val directions = PhotoListFragmentDirections.toSliderFromList(
                clickedPosition, binding.photoList!!.size
            )
            findNavController().navigate(directions)
        }

        viewModel.photoList.observe(viewLifecycleOwner) {
            binding.photoList = it
        }
    }
}
