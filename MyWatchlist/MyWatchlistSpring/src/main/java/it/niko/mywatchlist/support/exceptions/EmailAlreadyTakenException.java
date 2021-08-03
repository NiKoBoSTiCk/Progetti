package it.niko.mywatchlist.support.exceptions;

public class EmailAlreadyTakenException extends Exception{
    public EmailAlreadyTakenException(){
        super("Error: Email is already in use!");
    }
}
