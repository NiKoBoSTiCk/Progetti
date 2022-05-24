package it.niko.mywatchlistandroid

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private const val BASE_URL = "http://192.168.1.7:8000/"

        private val client = OkHttpClient.Builder().build()

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}