package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.repositories.UserRepository;
import it.niko.mywatchlist.support.exceptions.UserAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) throws UserAlreadyExistsException {
        if(userRepository.existsByEmail(user.getEmail()) || userRepository.existsByNickname(user.getNickname()))
            throw new UserAlreadyExistsException();
        userRepository.save(user);
    }

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
    public List<User> showUsersByNickname(String nickname, int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<User> pagedResult = userRepository.findByNicknameContaining(nickname, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public User showUserByEmail(String email) throws UserNotFoundException {
        if(!userRepository.existsByEmail(email))
            throw new UserNotFoundException(email);
        return userRepository.findByEmail(email);
    }
}
