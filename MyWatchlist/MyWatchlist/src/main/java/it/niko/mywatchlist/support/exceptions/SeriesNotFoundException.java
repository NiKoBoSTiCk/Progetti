package it.niko.mywatchlist.support.exceptions;

public class SeriesNotFoundException extends Exception {
    public SeriesNotFoundException(String title){
        super("Could not find the series [" + title + "] you were looking for");
    }
}
