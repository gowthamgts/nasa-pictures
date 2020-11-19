package dev.gowtham.nasapictures.repository

import android.content.Context
import com.appmattus.layercache.Cache
import com.appmattus.layercache.createLruCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.gowtham.nasapictures.JSON_CACHE_KEY_PREFIX
import dev.gowtham.nasapictures.model.PhotoModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

object JsonRepository {

    private val cache = Cache.createLruCache<String, PhotoModel>(30)

    fun loadJSONFromAsset(context: Context): List<PhotoModel> {
        val typeToken = object : TypeToken<List<PhotoModel>>() {}.type
        val inputStream = context.assets.open("data.json")
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        val json: List<PhotoModel> = Gson().fromJson(jsonString, typeToken)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return json.sortedByDescending { sdf.parse(it.date)!!.time }
    }

    suspend fun loadPhotoModel(context: Context, position: Int): PhotoModel {
        cache.get("${JSON_CACHE_KEY_PREFIX}_${position}")?.let {
            return it
        }
        Timber.w("photo cache was not present. reconstructing...")

        // construct the map and insert into cache
        val jsonList = loadJSONFromAsset(context)
        jsonList.forEachIndexed { index, element ->
            cache.set("${JSON_CACHE_KEY_PREFIX}_${index}", element)
        }

        return jsonList[position]
    }
}
