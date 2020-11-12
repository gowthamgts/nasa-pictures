package dev.gowtham.nasapictures.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.gowtham.nasapictures.NASAPicturesApp
import dev.gowtham.nasapictures.model.PhotoModel
import dev.gowtham.nasapictures.repository.JsonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoListViewModel(application: NASAPicturesApp) :
    AndroidViewModel(application), CoroutineScope by CoroutineScope(Dispatchers.IO) {

    val photoList = MutableLiveData<List<PhotoModel>>(emptyList())

    fun fetchPhotoList() {
        launch(Dispatchers.IO) {
            photoList.postValue(
                JsonRepository.loadJSONFromAsset(getApplication())
            )
        }
    }
}

class PhotoListViewModelFactory(private val application: NASAPicturesApp) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoListViewModel(application) as T
    }
}
