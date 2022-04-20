package it.niko.mywatchlistandroid.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("accessToken") val token: String
)
