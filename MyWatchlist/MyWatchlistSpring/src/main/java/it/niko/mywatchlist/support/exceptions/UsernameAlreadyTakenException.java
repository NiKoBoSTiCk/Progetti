package it.niko.mywatchlist.support.exceptions;

public class UsernameAlreadyTakenException extends Exception{
    public UsernameAlreadyTakenException(){
        super("Error: Username is already taken!");
    }
}
