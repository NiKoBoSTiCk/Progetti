package com.msl.myserieslist.controllers.rest;

import com.msl.myserieslist.entities.Actor;
import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.services.ActorService;
import com.msl.myserieslist.support.ResponseMessage;
import com.msl.myserieslist.support.exceptions.ActorAlreadyExistException;
import com.msl.myserieslist.support.exceptions.ActorNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/actor")
public class ActorController {
    @Autowired
    private ActorService actorService;


    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Actor actor) {
        try {
            actorService.addActor(actor);
        } catch (ActorAlreadyExistException e) {
            return new ResponseEntity<>(new ResponseMessage("Actor already exist!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Added successful!"), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity delete(@RequestBody @Valid Actor actor) {
        try {
            Actor removed = actorService.removeActor(actor);
            return new ResponseEntity<>(removed, HttpStatus.OK);
        } catch (ActorNotExistException e) {
            return new ResponseEntity<>(new ResponseMessage("Actor not found!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Actor> getAll() {
        return actorService.showAllActor();
    }

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Actor> result = actorService.showAllActor(pageNumber, pageSize, sortBy);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_first_name")
    public ResponseEntity getByFirstName(@RequestParam(required = false) String firstName) {
        List<Actor> result = actorService.showActorByFirstName(firstName);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_last_name")
    public ResponseEntity getByLastName(@RequestParam(required = false) String lastName) {
        List<Actor> result = actorService.showActorByLastName(lastName);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_career")
    public ResponseEntity getByCareer(@RequestParam(required = false) Series series) {
        List<Actor> result = actorService.showActorByCareer(series);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}