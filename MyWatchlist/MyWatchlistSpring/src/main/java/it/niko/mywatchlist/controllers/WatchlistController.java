package it.niko.mywatchlist.controllers;

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
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody @Valid WatchlistRequest watchlistRequest)
            throws SeriesAlreadyInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        watchlistService.addWatchlist(watchlistRequest);
        return ResponseEntity.ok(new MessageResponse("Added successful!"));
    }

    @PutMapping
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
                                          @RequestParam(value = "sortBy", defaultValue = "user") String sortBy) {
        List<Watchlist> ret = watchlistService.showAllWatchlists(pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/by_id/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getWatchlistByUser(@PathVariable int id) throws WatchlistNotFoundException {
        return ResponseEntity.ok(watchlistService.showWatchlistById(id));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getWatchlistByUser(@PathVariable String username,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "series") String sortBy)
            throws UserNotFoundException {
        List<Watchlist> ret = watchlistService.showUserWatchlist(username, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/{username}/{status}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getWatchlistByUserAndStatus(@PathVariable String username, @PathVariable String status,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "score") String sortBy)
            throws UserNotFoundException {
        List<Watchlist> ret = watchlistService.showUserWatchlistByStatus(username, status, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/{username}/by_score")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getWatchlistByUserAndScore(@PathVariable String username,
                                              @RequestParam(value = "score", defaultValue = "10") int score,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "score") String sortBy)
            throws UserNotFoundException {
        List<Watchlist> ret = watchlistService.showUserWatchlistByScore(username, score, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }
}
