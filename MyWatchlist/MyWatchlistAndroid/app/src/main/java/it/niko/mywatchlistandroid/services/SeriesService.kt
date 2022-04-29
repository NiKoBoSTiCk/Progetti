package it.niko.mywatchlistandroid.services

import it.niko.mywatchlistandroid.model.Series
import it.niko.mywatchlistandroid.payload.SeriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesService {

    @GET("/series/search/all")
    suspend fun getSeries(): Response<SeriesResponse>

    @GET("/series/search/by_views")
    suspend fun getSeriesByViews(): Response<ArrayList<Series>>

    @GET("/series/search/by_rating")
    suspend fun getSeriesByRating(): Response<ArrayList<Series>>

    @GET("/series/search/by_title")
    suspend fun getSeriesByTitle(@Query("title") title: String): Response<SeriesResponse>
}