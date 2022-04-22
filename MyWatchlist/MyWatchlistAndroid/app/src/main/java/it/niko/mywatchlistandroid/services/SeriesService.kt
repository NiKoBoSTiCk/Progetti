package it.niko.mywatchlistandroid.services

import it.niko.mywatchlistandroid.payload.SeriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface SeriesService {

    @GET("/series/search/all")
    suspend fun getSeries(): Response<SeriesResponse>
}