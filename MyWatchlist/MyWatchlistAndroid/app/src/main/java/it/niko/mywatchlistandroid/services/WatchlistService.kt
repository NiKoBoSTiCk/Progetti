package it.niko.mywatchlistandroid.services

import it.niko.mywatchlistandroid.payload.MessageResponse
import it.niko.mywatchlistandroid.payload.WatchlistRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WatchlistService {

    @POST("/watchlist")
    suspend fun addWatchlist(@Body watchlistRequest: WatchlistRequest): Response<MessageResponse>
}