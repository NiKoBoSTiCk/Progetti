package it.niko.mywatchlist.support.exceptions;

public class SeriesAlreadyInWatchlistException extends Exception{
    public SeriesAlreadyInWatchlistException(String username){
        super("Series already exists in [" + username + "] watchlist");
    }
}
