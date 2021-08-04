package it.niko.mywatchlist.controllers;

import it.niko.mywatchlist.payload.response.MessageResponse;
import it.niko.mywatchlist.support.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler({
            UserNotFoundException.class, UserAlreadyExistsException.class,
            SeriesNotFoundException.class, SeriesAlreadyExistsException.class,
            SeriesNotInWatchlistException.class, SeriesAlreadyInWatchlistException.class,
            //RuntimeException.class,
            UsernameAlreadyTakenException.class, EmailAlreadyTakenException.class
    })
    public final ResponseEntity<?> handleException(Exception ex){
        if(ex instanceof UserNotFoundException)
            return ResponseEntity.notFound().build();

        else if(ex instanceof UserAlreadyExistsException)
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));

        else if(ex instanceof SeriesNotFoundException)
            return ResponseEntity.notFound().build();

        else if(ex instanceof SeriesAlreadyExistsException)
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));

        else if(ex instanceof SeriesNotInWatchlistException)
            return ResponseEntity.notFound().build();

        else if (ex instanceof SeriesAlreadyInWatchlistException)
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));

        else if (ex instanceof UsernameAlreadyTakenException)
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));

        else if (ex instanceof EmailAlreadyTakenException)
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));

        else if (ex instanceof RuntimeException)
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));

        else return ResponseEntity.internalServerError().body(new MessageResponse(ex.getMessage()));
    }
}
