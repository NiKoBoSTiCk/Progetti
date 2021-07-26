package it.niko.mywatchlist.support.exceptions;

public class SeriesNotFoundException extends Exception {
    public SeriesNotFoundException(String name){
        super("Could not find the series [" + name + "] you were looking for");
    }
}
