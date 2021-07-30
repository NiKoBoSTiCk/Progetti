package it.niko.mywatchlist.support.exceptions;

import it.niko.mywatchlist.entities.EGenre;

public class GenreNotExistException extends Exception{
    public GenreNotExistException(EGenre name){
        super("Genre [" + name + "] not exist");
    }
}
