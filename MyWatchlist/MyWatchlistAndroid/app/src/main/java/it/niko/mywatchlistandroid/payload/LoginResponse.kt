package it.niko.mywatchlistandroid.payload

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("accessToken") val token: String
)
