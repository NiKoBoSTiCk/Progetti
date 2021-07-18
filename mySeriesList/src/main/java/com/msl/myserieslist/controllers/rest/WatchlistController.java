package com.msl.myserieslist.controllers.rest;


import com.msl.myserieslist.entities.Watchlist;
import com.msl.myserieslist.services.WatchlistService;
import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.support.ResponseMessage;
import com.msl.myserieslist.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/watchlist")
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity create(@RequestBody @Valid Watchlist watchlist) {
        try{
            watchlistService.addWatchlist(watchlist);
        } catch(UserNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        } catch(SeriesNotExistException es){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series not found!", es);
        } catch(WatchlistAlreadyExistException ew) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series already in your watchlist!", ew);
        }
        return new ResponseEntity<>(new ResponseMessage("Added successful!"), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity delete(@RequestBody @Valid Watchlist watchlist) {
        try{
            watchlistService.removeWatchlist(watchlist);
        } catch(UserNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        } catch(SeriesNotExistException es){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series not found!", es);
        } catch(WatchlistNotExistException ew) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series not in your watchlist!", ew);
        }
        return new ResponseEntity<>(new ResponseMessage("Removed successful!"), HttpStatus.OK);
    }

    @GetMapping("/{user}")
    public List<Watchlist> getWatchlist(@RequestBody @Valid User user) {
        try {
            return watchlistService.getWatchlistByUser(user);
        } catch(UserNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }

    @GetMapping("/{user}")
    public List<Watchlist> getWatchlistByStatus(@RequestBody @Valid User user, @RequestBody @Valid Watchlist.Status status) {
        try {
            return watchlistService.getWatchlistByUserAndStatus(user, status);
        } catch(UserNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }

    @GetMapping("/{user}")
    public List<Watchlist> getWatchlistByScore(@RequestBody @Valid User user, @RequestBody @Valid int score) {
        try {
            return watchlistService.getWatchlistByUserAndScore(user, score);
        } catch(UserNotExistException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        } catch(ScoreNotValidException es){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Score not valid!", es);
        }
    }
}
