package dev.gowtham.nasapictures.util

import dev.gowtham.nasapictures.NASAPicturesApp
import dev.gowtham.nasapictures.viewmodel.PhotoListViewModelFactory

object InjectorUtils {
    fun providePhotoViewModelFactory(application: NASAPicturesApp): PhotoListViewModelFactory {
        return PhotoListViewModelFactory(application)
    }
}
