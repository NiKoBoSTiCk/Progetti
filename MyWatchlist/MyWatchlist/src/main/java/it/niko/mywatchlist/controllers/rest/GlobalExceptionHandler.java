package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.support.ResponseMessage;
import it.niko.mywatchlist.support.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler({ UserNotFoundException.class, UserAlreadyExistsException.class,
                        SeriesNotFoundException.class, SeriesAlreadyExistsException.class,
                        SeriesNotInWatchlistException.class, SeriesAlreadyInWatchlistException.class,
                        ProgressNotValidException.class, ScoreNotValidException.class})
    public final ResponseEntity<?> handleException(Exception ex) {

        if(ex instanceof UserNotFoundException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.NOT_FOUND);

        } else if(ex instanceof UserAlreadyExistsException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);

        }  else if(ex instanceof SeriesNotFoundException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.NOT_FOUND);

        } else if(ex instanceof SeriesAlreadyExistsException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);

        } else if(ex instanceof SeriesNotInWatchlistException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.NOT_FOUND);

        } else if (ex instanceof SeriesAlreadyInWatchlistException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);

        } else if (ex instanceof ProgressNotValidException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);

        } else if (ex instanceof ScoreNotValidException){
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);

        } else {
            return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
