package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.payload.response.MessageResponse;
import it.niko.mywatchlist.services.UserService;
import it.niko.mywatchlist.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "username") String sortBy){
        List<User> ret = userService.showAllUsers(pageNumber, pageSize, sortBy);
        if(ret.size() != 0) return ResponseEntity.ok(ret);
        return ResponseEntity.ok(new MessageResponse("No results!"));
    }

    @GetMapping("/search/by_username")
    public ResponseEntity<?> getByUsername(@RequestParam String username) throws UserNotFoundException {
        User ret = userService.showUserByUsername(username);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/search/by_email")
    public ResponseEntity<?> getByEmail(@RequestParam String email) throws UserNotFoundException {
        User ret = userService.showUserByEmail(email);
        return ResponseEntity.ok(ret);
    }
}
