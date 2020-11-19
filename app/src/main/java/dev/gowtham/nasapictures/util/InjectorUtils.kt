package dev.gowtham.nasapictures.util

import dev.gowtham.nasapictures.NASAPicturesApp
import dev.gowtham.nasapictures.viewmodel.PhotoDetailViewModelFactory
import dev.gowtham.nasapictures.viewmodel.PhotoListViewModelFactory

object InjectorUtils {

    fun providePhotoListViewModelFactory(application: NASAPicturesApp): PhotoListViewModelFactory {
        return PhotoListViewModelFactory(application)
    }

    fun providePhotoDetailViewModelFactory(application: NASAPicturesApp): PhotoDetailViewModelFactory {
        return PhotoDetailViewModelFactory(application)
    }
}
