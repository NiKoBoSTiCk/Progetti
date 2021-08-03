package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.entities.Status;
import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.entities.Watchlist;
import it.niko.mywatchlist.payload.request.WatchlistRequest;
import it.niko.mywatchlist.payload.response.MessageResponse;
import it.niko.mywatchlist.services.WatchlistService;
import it.niko.mywatchlist.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody @Valid WatchlistRequest watchlistRequest)
            throws SeriesAlreadyInWatchlistException, UserNotFoundException, SeriesNotFoundException{
        watchlistService.addWatchlist(watchlistRequest);
        return ResponseEntity.ok(new MessageResponse("Added successful!"));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@RequestBody @Valid WatchlistRequest watchlistRequest)
            throws SeriesNotInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        watchlistService.updateWatchlist(watchlistRequest);
        return ResponseEntity.ok(new MessageResponse("Progress updated successful!"));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@RequestBody @Valid WatchlistRequest watchlistRequest)
            throws SeriesNotInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        watchlistService.removeWatchlist(watchlistRequest);
        return ResponseEntity.ok(new MessageResponse("Removed successful!"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getWatchlists(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Watchlist> ret = watchlistService.showAllWatchlists(pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getWatchlistByUser(@RequestBody @Valid User user,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Watchlist> ret = watchlistService.showUserWatchlist(user, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search/by_status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getWatchlistByUserAndStatus(@RequestBody @Valid User user,
                                              @RequestParam(value = "status", defaultValue = "WATCHING") Status status,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Watchlist> ret = watchlistService.showUserWatchlistByStatus(user, status, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search/by_score")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getWatchlistByUserAndScore(@RequestBody @Valid User user,
                                              @RequestParam(value = "score", defaultValue = "10") int score,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Watchlist> ret = watchlistService.showUserWatchlistByScore(user, score, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }
}
