package dev.gowtham.nasapictures

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.gowtham.nasapictures.model.PhotoModel
import dev.gowtham.nasapictures.repository.JsonRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class JsonRepositoryTest {

    @Test
    fun ensureJSONisProperlyLoaded_FromAssetFile() {
        // given
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        // when
        val data = JsonRepository.loadJSONFromAsset(context)

        // then
        assertThat(data).isNotEmpty
    }

    @Test
    fun ensureAllItemsAreLoaded_FromAssetFile() {
        // given
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        // when
        val data = JsonRepository.loadJSONFromAsset(context)

        // then
        assertThat(data).hasSize(26)
    }

    @Test
    fun ensureProperDeserialization_FromAssetFile() {
        // given
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        // when
        val data = JsonRepository.loadJSONFromAsset(context)

        // then
        assertThat(data).first().isExactlyInstanceOf(PhotoModel::class.java)
    }

    @Test
    fun ensureSortingOfDate_FromAssetFile() {
        // given
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        // when
        val data = JsonRepository.loadJSONFromAsset(context)

        // then
        assertThat(data).isSortedAccordingTo { o1, o2 ->
            val timeStamp1 = sdf.parse(o1.date)!!.time
            val timeStamp2 = sdf.parse(o2.date)!!.time
            if (timeStamp1 > timeStamp2) {
                return@isSortedAccordingTo -1
            } else if (timeStamp1 < timeStamp2) {
                return@isSortedAccordingTo 1
            }
            return@isSortedAccordingTo 0
        }
    }
}
