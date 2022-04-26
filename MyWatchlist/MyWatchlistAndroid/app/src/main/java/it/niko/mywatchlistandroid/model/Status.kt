package it.niko.mywatchlistandroid.model

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("name")
    var type: String
)
