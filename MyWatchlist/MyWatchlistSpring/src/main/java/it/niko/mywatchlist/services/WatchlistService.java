package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.*;
import it.niko.mywatchlist.repositories.StatusRepository;
import it.niko.mywatchlist.repositories.WatchlistRepository;
import it.niko.mywatchlist.repositories.SeriesRepository;
import it.niko.mywatchlist.repositories.UserRepository;
import it.niko.mywatchlist.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Transactional
    public void addWatchlist(Watchlist watchlist) throws SeriesAlreadyInWatchlistException, ProgressNotValidException, UserNotFoundException, SeriesNotFoundException, ScoreNotValidException {
        if(watchlistRepository.existsById(watchlist.getId()))
            throw new SeriesAlreadyInWatchlistException();
        if(!userRepository.existsById(watchlist.getUser().getId()))
            throw new UserNotFoundException(watchlist.getUser().getUsername());
        if(!seriesRepository.existsById(watchlist.getSeries().getId()))
            throw new SeriesNotFoundException(watchlist.getSeries().getTitle());
        if(watchlist.getProgress() < 0 ||  watchlist.getProgress() > watchlist.getSeries().getEpisodes())
            throw new ProgressNotValidException(watchlist.getProgress());
        if(watchlist.getScore() < 0 || watchlist.getScore() > 10)
            throw new ScoreNotValidException(watchlist.getScore());

        User user = userRepository.getById(watchlist.getUser().getId());
        Series series = seriesRepository.getById(watchlist.getSeries().getId());
        if(watchlistRepository.existsByUserAndSeries(user, series))
            throw new SeriesAlreadyInWatchlistException();

        series.increaseViews();
        if(watchlist.getScore() != 0)
            series.updateRating(watchlist.getScore(), false);
        if(watchlist.getSeries().getEpisodes() == watchlist.getProgress())
            watchlist.setStatus(statusRepository.findByName(EStatus.COMPLETED));
        watchlistRepository.save(watchlist);
    }

    @Transactional
    public void updateWatchlist(Watchlist watchlist) throws SeriesNotInWatchlistException, ProgressNotValidException, ScoreNotValidException, UserNotFoundException, SeriesNotFoundException {
        if(!watchlistRepository.existsById(watchlist.getId()))
            throw new SeriesNotInWatchlistException();
        if(!userRepository.existsById(watchlist.getUser().getId()))
            throw new UserNotFoundException(watchlist.getUser().getUsername());
        if(!seriesRepository.existsById(watchlist.getSeries().getId()))
            throw new SeriesNotFoundException(watchlist.getSeries().getTitle());

        Series series = seriesRepository.getById(watchlist.getSeries().getId());
        if(watchlist.getProgress() < 0 ||  watchlist.getProgress() > watchlist.getSeries().getEpisodes())
            throw new ProgressNotValidException(watchlist.getProgress());
        if(watchlist.getScore() < 0 || watchlist.getScore() > 10)
            throw new ScoreNotValidException(watchlist.getScore());
        if(watchlist.getScore() != 0)
            series.updateRating(watchlist.getScore(), false);

        watchlistRepository.findById(watchlist.getId()).map(wat -> {
            wat.setComment(watchlist.getComment());
            wat.setScore(watchlist.getScore());
            wat.setProgress(watchlist.getProgress());
            wat.setStatus(watchlist.getStatus());
            if(watchlist.getSeries().getEpisodes() == wat.getProgress())
                wat.setStatus(statusRepository.findByName(EStatus.COMPLETED));
            return watchlistRepository.save(wat);
        });
    }

    @Transactional
    public void removeWatchlist(Watchlist watchlist) throws SeriesNotInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        if(!watchlistRepository.existsById(watchlist.getId()))
            throw new SeriesNotInWatchlistException();
        if(!userRepository.existsById(watchlist.getUser().getId()))
            throw new UserNotFoundException(watchlist.getUser().getUsername());
        if(!seriesRepository.existsById(watchlist.getSeries().getId()))
            throw new SeriesNotFoundException(watchlist.getSeries().getTitle());
        if(watchlist.getScore() != 0){
            Series series = seriesRepository.getById(watchlist.getSeries().getId());
            series.updateRating(watchlist.getScore(), true);
        }
        watchlistRepository.delete(watchlist);
    }

    @Transactional(readOnly = true)
    public List<Watchlist> showAllWatchlists(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Watchlist> pagedResult = watchlistRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Watchlist> showUserWatchlist(User user, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Watchlist> pagedResult = watchlistRepository.findByUser(user, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Watchlist> showUserWatchlistByStatus(User user, Status status , int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Watchlist> pagedResult = watchlistRepository.findByUserAndStatus(user, status, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Watchlist> showUserWatchlistByScore(User user, int score , int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Watchlist> pagedResult = watchlistRepository.findByUserAndScoreLessThanEqual(user, score, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }
}
