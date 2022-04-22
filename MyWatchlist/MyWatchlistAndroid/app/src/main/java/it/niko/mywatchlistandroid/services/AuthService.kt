package it.niko.mywatchlistandroid.services

import it.niko.mywatchlistandroid.payload.LoginResponse
import it.niko.mywatchlistandroid.payload.LoginRequest
import it.niko.mywatchlistandroid.payload.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("/auth/signup")
    suspend fun signup(@Body body: SignupRequest)
}