package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.entities.SeriesInList;
import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.services.SeriesInListService;
import it.niko.mywatchlist.support.ResponseMessage;
import it.niko.mywatchlist.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class SeriesInListController {
    @Autowired
    SeriesInListService seriesInListService;

    @PostMapping
    @RolesAllowed("user")
    public ResponseEntity<?> create(@RequestBody @Valid SeriesInList seriesInList)
            throws SeriesAlreadyInWatchlistException, ProgressNotValidException,
            UserNotFoundException, SeriesNotFoundException, ScoreNotValidException {
        seriesInListService.addSeriesInList(seriesInList);
        return new ResponseEntity<>(new ResponseMessage("Added successful!"), HttpStatus.OK);
    }

    @PutMapping("/update")
    @RolesAllowed("user")
    public ResponseEntity<?> update(@RequestBody @Valid SeriesInList seriesInList)
            throws SeriesNotInWatchlistException, ProgressNotValidException,
            ScoreNotValidException, UserNotFoundException, SeriesNotFoundException {
        seriesInListService.updateSeriesInList(seriesInList);
        return new ResponseEntity<>(new ResponseMessage("Progress updated successful!"), HttpStatus.OK);
    }

    @DeleteMapping
    @RolesAllowed("user")
    public ResponseEntity<?> delete(@RequestBody @Valid SeriesInList seriesInList)
            throws SeriesNotInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        seriesInListService.removeSeriesInList(seriesInList);
        return new ResponseEntity<>(new ResponseMessage("Removed successful!"), HttpStatus.OK);
    }

    @GetMapping
    @RolesAllowed("admin")
    public ResponseEntity<?> getSeriesInList(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<SeriesInList> result = seriesInListService.showAllSeriesInList(pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search")
    @RolesAllowed("user")
    public ResponseEntity<?> getSeriesInListByUser(@RequestBody @Valid User user,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<SeriesInList> result = seriesInListService.showUserWatchlist(user, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_status")
    @RolesAllowed("user")
    public ResponseEntity<?> getSeriesInListByUser(@RequestBody @Valid User user,
                                              @RequestParam(value = "status", defaultValue = "WATCHING") SeriesInList.Status status,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<SeriesInList> result = seriesInListService.showUserWatchlist(user, status, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_score")
    @RolesAllowed("user")
    public ResponseEntity<?> getSeriesInListByUser(@RequestBody @Valid User user,
                                              @RequestParam(value = "score", defaultValue = "10") int score,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<SeriesInList> result = seriesInListService.showUserWatchlist(user, score, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
