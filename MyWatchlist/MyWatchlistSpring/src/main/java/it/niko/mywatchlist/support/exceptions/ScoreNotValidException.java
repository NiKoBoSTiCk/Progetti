package it.niko.mywatchlist.support.exceptions;

public class ScoreNotValidException extends Exception{
    public ScoreNotValidException(int score){
        super("The score [" + score + "] is not valid");
    }
}
