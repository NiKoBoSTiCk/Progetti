package it.niko.mywatchlist.controllers.rest;

import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.services.UserService;
import it.niko.mywatchlist.support.ResponseMessage;
import it.niko.mywatchlist.support.exceptions.UserAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @RolesAllowed("admin")
    public ResponseEntity<?> create(@RequestBody @Valid User user) throws UserAlreadyExistsException {
        userService.addUser(user);
        return new ResponseEntity<>(new ResponseMessage("Added successful!"), HttpStatus.OK);
    }

    @GetMapping
    @RolesAllowed("admin")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "nickname") String sortBy){
        List<User> result = userService.showAllUsers(pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_nickname")
    @RolesAllowed("admin")
    public ResponseEntity<?> getByNickname(@RequestParam(required = false) String nickname,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "nickname") String sortBy){
        List<User> result = userService.showUsersByNickname(nickname, pageNumber, pageSize, sortBy);
        if(result.size() <= 0) return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_email")
    @RolesAllowed("admin")
    public ResponseEntity<?> getByEmail(@RequestParam String email){
        User result;
        try {
            result = userService.showUserByEmail(email);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage("User not found!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
