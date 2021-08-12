package it.niko.mywatchlist.controllers;

import it.niko.mywatchlist.entities.Series;
import it.niko.mywatchlist.payload.request.SeriesRequest;
import it.niko.mywatchlist.payload.response.MessageResponse;
import it.niko.mywatchlist.services.SeriesService;
import it.niko.mywatchlist.support.exceptions.SeriesAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.SeriesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/series")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid SeriesRequest seriesRequest) throws SeriesAlreadyExistsException {
        seriesService.addSeries(seriesRequest);
        return ResponseEntity.ok(new MessageResponse("Added successful!"));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestParam(value = "title") String title) throws SeriesNotFoundException {
        seriesService.removeSeries(title);
        return ResponseEntity.ok(new MessageResponse("Removed successful!"));
    }

    @PutMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> update(@RequestBody @Valid SeriesRequest seriesRequest) throws SeriesNotFoundException {
        seriesService.updateSeries(seriesRequest);
        return ResponseEntity.ok(new MessageResponse("Updated successful!"));
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        Page<Series> ret = seriesService.showAllSeries(pageNumber, pageSize, sortBy);
        if(ret.hasContent()) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getAll(@PathVariable int id) throws SeriesNotFoundException {
        return ResponseEntity.ok(seriesService.showSeriesById(id));
    }

    @GetMapping("/search/by_rating")
    public ResponseEntity<?> getByRating(@RequestParam(value = "rating", defaultValue = "10") int rating,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = "rating") String sortBy){
        List<Series> ret = seriesService.showSeriesByRating(rating, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search/by_views")
    public ResponseEntity<?> getByViews(@RequestParam(value = "minViews", defaultValue = "0") int minViews,
                                     @RequestParam(value = "maxViews", defaultValue = "100000") int maxViews,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = "views") String sortBy){
        List<Series> ret = seriesService.showSeriesByViews(minViews, maxViews, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search/by_title")
    public ResponseEntity<?> getByTitle(@RequestParam(value = "title") String title,
                                    @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Series> ret = seriesService.showSeriesByTitle(title, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search/by_genre")
    public ResponseEntity<?> getByGenre(@RequestParam(value = "genre", defaultValue = "drama") String genre,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "title") String sortBy){
        List<Series> ret = seriesService.showSeriesByGenres(genre, pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }
}
