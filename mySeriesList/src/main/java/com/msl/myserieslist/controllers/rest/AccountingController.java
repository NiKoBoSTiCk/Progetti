package com.msl.myserieslist.controllers.rest;

import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.entities.Watchlist;
import com.msl.myserieslist.services.AccountingService;
import com.msl.myserieslist.support.ResponseMessage;
import com.msl.myserieslist.support.exceptions.MailUserAlreadyExistsException;
import com.msl.myserieslist.support.exceptions.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class AccountingController {
    @Autowired
    private AccountingService accountingService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid User user) {
        try {
            User added = accountingService.registerUser(user);
            return new ResponseEntity(added, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("user already exist!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity delete(@RequestBody @Valid User user) {
        try {
            User removed = accountingService.removeUser(user);
            return new ResponseEntity(removed, HttpStatus.OK);
        } catch (UserNotExistException e) {
            return new ResponseEntity<>(new ResponseMessage("user not found!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getWatchlist(@RequestBody @Valid User user) {
        try {
            List<Watchlist> watchlist = accountingService.getUserWatchlist(user);
            return new ResponseEntity(watchlist, HttpStatus.OK);
        } catch (UserNotExistException e) {
            return new ResponseEntity<>(new ResponseMessage("user not found!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<User> getAll() {
        return accountingService.getAllUsers();
    }

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<User> result = accountingService.showAllUsers(pageNumber, pageSize, sortBy);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_first_name")
    public ResponseEntity getByFirstName(@RequestParam(required = false) String firstName) {
        List<User> result = accountingService.showUserByFirstName(firstName);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_last_name")
    public ResponseEntity getByLastName(@RequestParam(required = false) String lastName) {
        List<User> result = accountingService.showUserByLastName(lastName);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_email")
    public ResponseEntity getByEmail(@RequestParam(required = false) String email) {
        List<User> result = accountingService.showUserByEmail(email);
        if(result.size() <= 0){
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
