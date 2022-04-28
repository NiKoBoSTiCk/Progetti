package it.niko.mywatchlistandroid.model

import com.google.gson.annotations.SerializedName

data class Series(
    @SerializedName("title")
    val title: String,

    @SerializedName("episodes")
    val episodes: Int,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("views")
    val views: Int,

    @SerializedName("plot")
    val plot: String,

    @SerializedName("members")
    val members: Int,


    @SerializedName("genres")
    val genres: ArrayList<Genre>
)
