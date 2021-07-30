package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.repositories.UserRepository;
import it.niko.mywatchlist.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> showAllUsers(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<User> pagedResult = userRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public Optional<User> showUserByUsername(String username) throws UserNotFoundException {
        if(!userRepository.existsByUsername(username))
            throw new UserNotFoundException(username);
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> showUserByEmail(String email) throws UserNotFoundException {
        if(!userRepository.existsByEmail(email))
            throw new UserNotFoundException(email);
        return userRepository.findByEmail(email);
    }
}
