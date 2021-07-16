package com.msl.myserieslist.controllers.rest;


import com.msl.myserieslist.entities.Watchlist;
import com.msl.myserieslist.services.WatchlistService;
import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.services.SeriesService;
import com.msl.myserieslist.support.ResponseMessage;
import com.msl.myserieslist.support.exceptions.UserNotExistException;
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
        return new ResponseEntity<>(WatchlistService.addSeries(series), HttpStatus.OK);
    }

    @GetMapping("/{user}")
    public List<Series> getSeries(@RequestBody @Valid User user) {
        try {
            return watchlistService.getWatchlistByUser(user);
        } catch (UserNotExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }
}
