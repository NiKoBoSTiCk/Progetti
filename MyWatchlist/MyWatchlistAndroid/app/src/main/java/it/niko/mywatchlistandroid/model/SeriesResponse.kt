package it.niko.mywatchlistandroid.model

import com.google.gson.annotations.SerializedName

data class SeriesResponse (
    @SerializedName("content")
    val seriesList: ArrayList<Series>
)