package com.msl.myserieslist.controllers.rest;


import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.services.SeriesService;
import com.msl.myserieslist.support.ResponseMessage;
import com.msl.myserieslist.support.exceptions.SeriesAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/products")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;


    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Series series) {
        try {
            seriesService.addSeries(series);
        } catch (SeriesAlreadyExistException e) {
            return new ResponseEntity<>(new ResponseMessage("Barcode already exist!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Added successful!"), HttpStatus.OK);
    }

    @GetMapping
    public List<Series> getAll() {
        return seriesService.showAllSeries();
    }

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Series> result = seriesService.showAllSeries(pageNumber, pageSize, sortBy);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_name")
    public ResponseEntity getByName(@RequestParam(required = false) String name) {
        List<Series> result = seriesService.showSeriesByName(name);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
