package it.niko.mywatchlist.support.exceptions;

public class ProgressNotValidException extends Exception{
    public ProgressNotValidException(int progress){
        super("Progress [" + progress + "] not valid");
    }
}
