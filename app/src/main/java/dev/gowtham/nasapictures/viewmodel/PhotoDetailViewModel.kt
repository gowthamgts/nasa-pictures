package dev.gowtham.nasapictures.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.gowtham.nasapictures.NASAPicturesApp
import dev.gowtham.nasapictures.model.PhotoModel
import dev.gowtham.nasapictures.repository.JsonRepository
import dev.gowtham.nasapictures.repository.RemoteImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoDetailViewModel(private val application: NASAPicturesApp) :
    AndroidViewModel(application), CoroutineScope by CoroutineScope(Dispatchers.IO) {

    val currentPhotoModel = MutableLiveData<PhotoModel>()
    val largePhotoModel = MutableLiveData<Bitmap>()

    fun fetchPhoto(position: Int) {
        launch(Dispatchers.IO) {
            val photoModel = JsonRepository.loadPhotoModel(application, position)
            currentPhotoModel.postValue(photoModel)
            largePhotoModel.postValue(
                RemoteImageRepository.getInstance(application).getHDImage(photoModel.hdUrl)
            )
        }
    }
}

class PhotoDetailViewModelFactory(private val application: NASAPicturesApp) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoDetailViewModel(application) as T
    }
}
