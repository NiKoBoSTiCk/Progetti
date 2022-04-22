package it.niko.mywatchlistandroid.payload

import com.google.gson.annotations.SerializedName

data class WatchlistRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("progress")
    val progress: Int,

    @SerializedName("score")
    val score: Int,

    @SerializedName("comment")
    val comment: String,
)
