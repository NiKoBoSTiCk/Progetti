package it.niko.mywatchlist.support.exceptions;

public class SeriesAlreadyExistsException extends Exception {
    public SeriesAlreadyExistsException(){
        super("Series already exists");
    }
}
