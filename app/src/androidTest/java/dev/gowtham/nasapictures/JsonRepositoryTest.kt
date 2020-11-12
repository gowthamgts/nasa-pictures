package dev.gowtham.nasapictures

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.gowtham.nasapictures.model.PhotoModel
import dev.gowtham.nasapictures.repository.JsonRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JsonRepositoryTest {

    @Test
    fun ensureJSONisProperlyLoaded_FromAsset() {
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
}
