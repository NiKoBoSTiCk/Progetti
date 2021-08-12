package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.ERole;
import it.niko.mywatchlist.entities.Role;
import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.payload.request.LoginRequest;
import it.niko.mywatchlist.payload.request.SignupRequest;
import it.niko.mywatchlist.payload.response.JwtResponse;
import it.niko.mywatchlist.repositories.RoleRepository;
import it.niko.mywatchlist.repositories.UserRepository;
import it.niko.mywatchlist.support.exceptions.EmailAlreadyTakenException;
import it.niko.mywatchlist.support.exceptions.UserNotFoundException;
import it.niko.mywatchlist.support.exceptions.UsernameAlreadyTakenException;
import it.niko.mywatchlist.support.security.jwt.JwtUtils;
import it.niko.mywatchlist.support.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @Transactional
    public JwtResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @Transactional
    public void signup(SignupRequest signUpRequest) throws UsernameAlreadyTakenException, EmailAlreadyTakenException {
        if(userRepository.existsByUsername(signUpRequest.getUsername()))
            throw new UsernameAlreadyTakenException();
        if(userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new EmailAlreadyTakenException();
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = assignRoles(strRoles);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> showAllUsers(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return userRepository.findAll(paging).getContent();
    }

    @Transactional(readOnly = true)
    public User showUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User showUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    private Set<Role> assignRoles(Set<String> strRoles){
        Set<Role> roles = new HashSet<>();
        if(strRoles == null){
            Role user = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role user not found."));
            roles.add(user);
        }
        else strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role admin = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role admin not found."));
                    roles.add(admin);
                    break;
                case "moderator":
                    Role moderator = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role moderator not found."));
                    roles.add(moderator);
                    break;
                default:
                    Role user = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role user not found."));
                    roles.add(user);
            }
        });
        return roles;
    }
}
