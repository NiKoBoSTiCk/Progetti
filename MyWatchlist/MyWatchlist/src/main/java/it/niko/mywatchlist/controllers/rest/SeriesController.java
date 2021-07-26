package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.entities.Series;
import it.niko.mywatchlist.services.SeriesService;
import it.niko.mywatchlist.support.ResponseMessage;
import it.niko.mywatchlist.support.exceptions.SeriesAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.SeriesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @PostMapping
    @RolesAllowed("admin")
    public ResponseEntity<?> create(@RequestBody @Valid Series series) throws SeriesAlreadyExistsException {
        seriesService.addSeries(series);
        return new ResponseEntity<>(new ResponseMessage("Added successful!"), HttpStatus.OK);
    }

    @DeleteMapping
    @RolesAllowed("admin")
    public ResponseEntity<?> delete(@RequestBody @Valid Series series) throws SeriesNotFoundException {
        seriesService.removeSeries(series);
        return new ResponseEntity<>(new ResponseMessage("Removed successful!"), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<Series> result = seriesService.showAllSeries(pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_rating")
    public ResponseEntity<?> getByRating(@RequestParam(value = "minRating", defaultValue = "0") int minRating,
                                     @RequestParam(value = "maxRating", defaultValue = "10") int maxRating,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<Series> result = seriesService.showSeriesByRating(minRating, maxRating, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_views")
    public ResponseEntity<?> getByViews(@RequestParam(value = "minViews", defaultValue = "0") int minViews,
                                     @RequestParam(value = "maxViews", defaultValue = "1000") int maxViews,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<Series> result = seriesService.showSeriesByViews(minViews, maxViews, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_name")
    public ResponseEntity<?> getByName(@RequestParam(required = false) String name,
                                    @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<Series> result = seriesService.showSeriesByName(name, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
