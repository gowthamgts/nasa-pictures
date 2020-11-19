package dev.gowtham.nasapictures.model

import com.google.gson.annotations.SerializedName

data class PhotoModel(
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("hdurl") val hdUrl: String,
    @SerializedName("date") val date: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("service_version") val serviceVersion: String
) {
    @SerializedName("copyright")
    var copyright: String? = null

    override fun toString(): String {
        return "$title - $url - $date"
    }
}
