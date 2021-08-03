package it.niko.mywatchlist.controllers;

import javax.validation.Valid;
import it.niko.mywatchlist.payload.request.LoginRequest;
import it.niko.mywatchlist.payload.request.SignupRequest;
import it.niko.mywatchlist.payload.response.JwtResponse;
import it.niko.mywatchlist.payload.response.MessageResponse;
import it.niko.mywatchlist.services.UserService;
import it.niko.mywatchlist.support.exceptions.EmailAlreadyTakenException;
import it.niko.mywatchlist.support.exceptions.UsernameAlreadyTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse ret = userService.login(loginRequest);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest)
            throws EmailAlreadyTakenException, UsernameAlreadyTakenException {
        userService.signup(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
