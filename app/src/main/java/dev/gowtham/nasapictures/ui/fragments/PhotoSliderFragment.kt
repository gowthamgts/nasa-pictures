package dev.gowtham.nasapictures.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.gowtham.nasapictures.ARG_POSITION
import dev.gowtham.nasapictures.databinding.PhotoSliderFragmentBinding

class PhotoSliderFragment : Fragment() {

    private var currentPosition = -1

    private lateinit var binding: PhotoSliderFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPosition = PhotoSliderFragmentArgs.fromBundle(requireArguments()).position
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return PhotoSliderFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.photoViewPager.adapter = PhotoPagerAdapter(this)
        binding.photoViewPager.setCurrentItem(currentPosition, false)
    }

    class PhotoPagerAdapter(
        private val fragment: PhotoSliderFragment
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int =
            PhotoSliderFragmentArgs.fromBundle(fragment.requireArguments()).totalItems

        override fun createFragment(position: Int): Fragment {
            val fragment = PhotoDetailFragment()
            fragment.arguments = Bundle().apply {
                // pass the position of the image
                putInt(ARG_POSITION, position)
            }
            return fragment
        }
    }
}
