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
import org.springframework.transaction.annotation.Transactional;
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
    public Watchlist addWatchlist(Watchlist watchlist) throws WatchlistAlreadyExistException, UserNotExistException, SeriesNotExistException {
        Watchlist ret = watchlistRepository.save(watchlist);
        User user = watchlist.getUser();
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        Series series = watchlist.getSeries();
        if(!seriesRepository.existsByName(series.getName()))
            throw new SeriesNotExistException();
        if(watchlistRepository.existsByUserAndSeries(user, series))
            throw new WatchlistAlreadyExistException();
        return ret;
    }

    @Transactional(readOnly = false)
    public Watchlist removeWatchlist(Watchlist watchlist) throws WatchlistNotExistException, UserNotExistException, SeriesNotExistException {
        watchlistRepository.delete(watchlist);
        User user = watchlist.getUser();
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        Series series = watchlist.getSeries();
        if(!seriesRepository.existsByName(series.getName()))
            throw new SeriesNotExistException();
        if(watchlistRepository.existsByUserAndSeries(user, series))
            throw new WatchlistNotExistException();
        return watchlist;
    }

    @Transactional(readOnly = true)
    public List<Watchlist> getWatchlistByUser(User user) throws UserNotExistException {
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        return watchlistRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Watchlist> getWatchlistByUserAndStatus(User user, Watchlist.Status status) throws UserNotExistException {
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        return watchlistRepository.findByUserAndStatus(user, status);
    }

    @Transactional(readOnly = true)
    public List<Watchlist> getWatchlistByUserAndScore(User user, int score) throws UserNotExistException, ScoreNotValidException {
        if(!userRepository.existsById(user.getIdUser()))
            throw new UserNotExistException();
        if(score<0 || score>10)
            throw new ScoreNotValidException();
        return watchlistRepository.findByUserAndScore(user, score);
    }
}
