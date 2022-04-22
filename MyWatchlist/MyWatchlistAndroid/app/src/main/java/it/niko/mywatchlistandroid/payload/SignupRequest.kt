package it.niko.mywatchlistandroid.payload

import com.google.gson.annotations.SerializedName

data class SignupRequest (

    @SerializedName("email")
    var email: String,

    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String
)