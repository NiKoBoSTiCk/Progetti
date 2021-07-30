package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.entities.Genre;
import it.niko.mywatchlist.entities.Series;
import it.niko.mywatchlist.payload.response.MessageResponse;
import it.niko.mywatchlist.services.SeriesService;
import it.niko.mywatchlist.support.exceptions.GenreNotExistException;
import it.niko.mywatchlist.support.exceptions.SeriesAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.SeriesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid Series series) throws SeriesAlreadyExistsException {
        seriesService.addSeries(series);
        return new ResponseEntity<>(new MessageResponse("Added successful!"), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestBody @Valid Series series) throws SeriesNotFoundException {
        seriesService.removeSeries(series);
        return new ResponseEntity<>(new MessageResponse("Removed successful!"), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> update(@RequestBody @Valid Series series) throws SeriesNotFoundException {
        seriesService.updateSeries(series);
        return new ResponseEntity<>(new MessageResponse("Updated successful!"), HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Series> result = seriesService.showAllSeries(pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new MessageResponse("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_rating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByRating(@RequestParam(value = "minRating", defaultValue = "0") int rating,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Series> result = seriesService.showSeriesByRating(rating, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new MessageResponse("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_views")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByViews(@RequestParam(value = "minViews", defaultValue = "0") int minViews,
                                     @RequestParam(value = "maxViews", defaultValue = "100000") int maxViews,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Series> result = seriesService.showSeriesByViews(minViews, maxViews, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new MessageResponse("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_title")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByTitle(@RequestParam(required = false) String title,
                                    @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Series> result = seriesService.showSeriesByTitle(title, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new MessageResponse("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_genres")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByGenre(@RequestBody Genre genre,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "title") String sortBy)
            throws GenreNotExistException {
        List<Series> result = seriesService.showSeriesByGenres(genre, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new MessageResponse("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
