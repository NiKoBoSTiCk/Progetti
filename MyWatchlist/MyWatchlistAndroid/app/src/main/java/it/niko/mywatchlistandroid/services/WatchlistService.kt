package it.niko.mywatchlistandroid.services

import it.niko.mywatchlistandroid.model.Watchlist
import it.niko.mywatchlistandroid.payload.MessageResponse
import it.niko.mywatchlistandroid.payload.WatchlistRequest

import retrofit2.Response
import retrofit2.http.*

interface WatchlistService {

    @POST("/watchlist")
    suspend fun addWatchlist(@Header("Authorization") token: String, @Body watchlistRequest: WatchlistRequest): Response<MessageResponse>

    @GET("/watchlist/{username}")
    suspend fun getWatchlist(@Header("Authorization") token: String, @Path("username") username: String): Response<ArrayList<Watchlist>>
}