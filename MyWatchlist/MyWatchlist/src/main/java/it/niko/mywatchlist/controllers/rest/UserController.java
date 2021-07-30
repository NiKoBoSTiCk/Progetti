package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.payload.response.MessageResponse;
import it.niko.mywatchlist.services.UserService;
import it.niko.mywatchlist.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "username") String sortBy){
        List<User> result = userService.showAllUsers(pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new MessageResponse("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_username")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> getByUsername(@RequestParam String username){
        Optional<User> result;
        try {
            result = userService.showUserByUsername(username);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(new  MessageResponse("User not found!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> getByEmail(@RequestParam String email){
        Optional<User> result;
        try {
            result = userService.showUserByEmail(email);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(new  MessageResponse("User not found!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
