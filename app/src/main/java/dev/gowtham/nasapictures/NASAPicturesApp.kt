package dev.gowtham.nasapictures

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class NASAPicturesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
