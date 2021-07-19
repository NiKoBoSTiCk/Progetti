package com.msl.myserieslist.services;

import com.msl.myserieslist.entities.Actor;
import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.entities.Watchlist;
import com.msl.myserieslist.repositories.UserRepository;
import com.msl.myserieslist.support.exceptions.MailUserAlreadyExistsException;
import com.msl.myserieslist.support.exceptions.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountingService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User registerUser(User user) throws MailUserAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new MailUserAlreadyExistsException();
        return userRepository.save(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User removeUser(User user) throws UserNotExistException {
        if (!userRepository.existsByEmail(user.getEmail()))
            throw new UserNotExistException();
        userRepository.delete(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Watchlist> getUserWatchlist(User user) throws UserNotExistException {
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        return user.getWatchlist();
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> showAllUsers(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        Page<User> pagedResult = userRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<User> showUserByFirstName(String firstName){
        return userRepository.findByFirstName(firstName);
    }

    @Transactional(readOnly = true)
    public List<User> showUserByLastName(String lastName){
        return userRepository.findByLastName(lastName);
    }

    @Transactional(readOnly = true)
    public List<User> showUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
