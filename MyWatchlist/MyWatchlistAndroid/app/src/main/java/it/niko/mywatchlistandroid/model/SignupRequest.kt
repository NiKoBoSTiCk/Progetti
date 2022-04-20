package it.niko.mywatchlistandroid.model

import com.google.gson.annotations.SerializedName

data class SignupRequest (

    @SerializedName("email")
    var email: String,

    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String
)