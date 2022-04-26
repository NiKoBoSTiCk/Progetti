package it.niko.mywatchlistandroid.payload

import it.niko.mywatchlistandroid.model.Watchlist

data class WatchlistResponse(

    val watchlist: ArrayList<Watchlist>
)