package dev.gowtham.nasapictures.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.appmattus.layercache.Cache
import com.appmattus.layercache.createDiskLruCache
import dev.gowtham.nasapictures.HD_IMAGE_CACHE_KEY_PREFIX
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL
import java.util.Locale

class RemoteImageRepository private constructor(context: Context) {

    private val cache = Cache.createDiskLruCache(
        File("${context.cacheDir}/remote-cache"), 25 * 1024 * 1024
    )

    suspend fun getHDImage(imageUrl: String): Bitmap {
        val uriLastSegment = Uri.parse(imageUrl).lastPathSegment?.replace(".", "_")
        val cacheKey = "${HD_IMAGE_CACHE_KEY_PREFIX}_${uriLastSegment?.toLowerCase(Locale.ROOT)}"
//        cache.get(cacheKey)?.let { cacheValue ->
//            cacheValue.byteInputStream(Charsets.UTF_8).also { bais ->
//                return bytesToBitmap(bais.readBytes())
//                Timber.i("cache hit $cacheValue")
//            }
//        }

        Timber.w("HD image not present in cache. downloading...")

        @Suppress("BlockingMethodInNonBlockingContext")
        URL(imageUrl).openStream().use { input ->
            ByteArrayOutputStream().use { baos ->
                input.copyTo(baos)
                cache.set(cacheKey, String(baos.toByteArray(), Charsets.UTF_8))

                return bytesToBitmap(baos.toByteArray())
            }
        }
    }

    private fun bytesToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    companion object {
        @Volatile
        private var instance: RemoteImageRepository? = null

        fun getInstance(context: Context): RemoteImageRepository {
            return instance ?: synchronized(this) {
                return instance ?: RemoteImageRepository(context)
            }
        }
    }
}
