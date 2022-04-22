package it.niko.mywatchlistandroid.payload

import com.google.gson.annotations.SerializedName
import it.niko.mywatchlistandroid.model.Series

data class SeriesResponse (
    @SerializedName("content")
    val seriesList: ArrayList<Series>
)