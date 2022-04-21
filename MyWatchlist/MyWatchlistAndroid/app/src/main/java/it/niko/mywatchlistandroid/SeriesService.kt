package it.niko.mywatchlistandroid

import it.niko.mywatchlistandroid.model.Series
import it.niko.mywatchlistandroid.model.SeriesList
import it.niko.mywatchlistandroid.model.SeriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface SeriesService {

    @GET("/series/search/all")
    suspend fun getSeries(): Response<SeriesResponse>
}