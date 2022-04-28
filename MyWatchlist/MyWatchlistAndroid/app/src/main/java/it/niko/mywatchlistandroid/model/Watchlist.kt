package it.niko.mywatchlistandroid.model

import com.google.gson.annotations.SerializedName

data class Watchlist  (
    @SerializedName("series")
    var series: Series,

    @SerializedName("status")
    var status: Status,

    @SerializedName("progress")
    var progress: Int,

    @SerializedName("score")
    var score: Int,

    @SerializedName("comment")
    var comment: String
)
