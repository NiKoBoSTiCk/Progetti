package com.msl.myserieslist.services;

import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.entities.Watchlist;
import com.msl.myserieslist.repositories.SeriesRepository;
import com.msl.myserieslist.repositories.UserRepository;
import com.msl.myserieslist.repositories.WatchlistRepository;
import com.msl.myserieslist.support.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class WatchlistService {
    @Autowired
    WatchlistRepository watchlistRepository;
    @Autowired
    SeriesRepository seriesRepository;
    @Autowired
    UserRepository userRepository;


    @Transactional(readOnly = false)
    public Watchlist addWatchlist(Watchlist watchlist) throws WatchlistAlreadyInWatchlistException {
        //TODO
        return null;
    }

    @Transactional(readOnly = false)
    public void removeSeriesFromWatchlist(Watchlist watchlist) {
        //TODO
    }

    @Transactional(readOnly = true)
    public List<Watchlist> showUserWatchlist(User user) throws UserNotExistException {
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        return watchlistRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Watchlist> showUserWatchlistByStatus(User user, Watchlist.Status status) throws UserNotExistException {
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        return watchlistRepository.findByUserAndStatus(user, status);
    }

    @Transactional(readOnly = true)
    public List<Watchlist> showUserWatchlistByScore(User user, int score) throws UserNotExistException, ScoreNotValidException {
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        if(score<0 || score>10)
            throw new ScoreNotValidException();
        return watchlistRepository.findByUserAndScore(user, score);
    }

}
