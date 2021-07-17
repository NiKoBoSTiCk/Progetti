package com.msl.myserieslist.services;

import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.entities.Watchlist;
import com.msl.myserieslist.repositories.UserRepository;
import com.msl.myserieslist.support.exceptions.MailUserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public List<Watchlist> getUserWatchlist(User user){
        if(userRepository.existsById(user.getIdUser()))
            return user.getWatchlist();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
