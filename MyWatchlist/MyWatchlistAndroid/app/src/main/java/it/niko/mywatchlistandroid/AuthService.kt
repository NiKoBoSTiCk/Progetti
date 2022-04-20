package it.niko.mywatchlistandroid

import it.niko.mywatchlistandroid.model.LoginResponse
import it.niko.mywatchlistandroid.model.LoginRequest
import it.niko.mywatchlistandroid.model.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("/auth/signup")
    suspend fun signup(@Body body: SignupRequest)
}