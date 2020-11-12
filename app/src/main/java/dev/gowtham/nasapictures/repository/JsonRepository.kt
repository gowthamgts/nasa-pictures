package dev.gowtham.nasapictures.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.gowtham.nasapictures.model.PhotoModel

object JsonRepository {

    fun loadJSONFromAsset(context: Context): List<PhotoModel> {
        val typeToken = object : TypeToken<List<PhotoModel>>() {}.type
        val inputStream = context.assets.open("data.json")
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        return Gson().fromJson(jsonString, typeToken)
    }
}
