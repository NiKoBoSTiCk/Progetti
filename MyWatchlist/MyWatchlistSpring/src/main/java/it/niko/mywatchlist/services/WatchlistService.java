package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.*;
import it.niko.mywatchlist.payload.request.WatchlistRequest;
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
    public void addWatchlist(WatchlistRequest wRequest) throws SeriesAlreadyInWatchlistException, UserNotFoundException, SeriesNotFoundException{
        User user = userRepository.findByUsername(wRequest.getUsername()).orElseThrow(UserNotFoundException::new);
        Series series = seriesRepository.findByTitle(wRequest.getTitle()).orElseThrow(SeriesNotFoundException::new);
        if(watchlistRepository.existsByUserAndSeries(user, series))
            throw new SeriesAlreadyInWatchlistException(user.getUsername());

        Watchlist watchlist = new Watchlist(user, series);
        configureWatchlist(wRequest, series, watchlist);
        series.increaseViews();
        watchlistRepository.save(watchlist);
    }

    @Transactional
    public void updateWatchlist(WatchlistRequest wRequest) throws SeriesNotInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        User user = userRepository.findByUsername(wRequest.getUsername()).orElseThrow(UserNotFoundException::new);
        Series series = seriesRepository.findByTitle(wRequest.getTitle()).orElseThrow(SeriesNotFoundException::new);
        Watchlist watchlist = watchlistRepository.findByUserAndSeries(user, series).orElseThrow(SeriesNotInWatchlistException::new);

        configureWatchlist(wRequest, series, watchlist);
        watchlistRepository.save(watchlist);
    }

    @Transactional
    public void removeWatchlist(WatchlistRequest wRequest) throws SeriesNotInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        User user = userRepository.findByUsername(wRequest.getUsername()).orElseThrow(UserNotFoundException::new);
        Series series = seriesRepository.findByTitle(wRequest.getTitle()).orElseThrow(SeriesNotFoundException::new);
        Watchlist watchlist = watchlistRepository.findByUserAndSeries(user, series).orElseThrow(SeriesNotInWatchlistException::new);

        if(watchlist.getScore() != 0)
            series.updateRating(watchlist.getScore(), true);
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

    private void configureWatchlist(WatchlistRequest wRequest, Series series, Watchlist watchlist) {
        if(wRequest.getProgress() < 0)
            watchlist.setProgress(0);
        else if(wRequest.getProgress() > series.getEpisodes())
            watchlist.setProgress(series.getEpisodes());
        else watchlist.setProgress(wRequest.getProgress());

        if(wRequest.getScore() < 0)
            watchlist.setScore(0);
        else watchlist.setScore(Math.min(wRequest.getScore(), 10));

        if(watchlist.getScore() != 0)
            series.updateRating(watchlist.getScore(), false);

        if(wRequest.getComment() != null)
            watchlist.setComment(wRequest.getComment());

        assignStatus(watchlist, series, wRequest.getStatus());

        if(series.getEpisodes() == watchlist.getProgress())
            watchlist.setStatus(statusRepository.findByName(EStatus.COMPLETED)
                    .orElseThrow(() -> new RuntimeException("Error: Status completed not found!")));
    }

    private void assignStatus(Watchlist watchlist, Series series, String status) {
        if(status == null){
            Status watching = statusRepository.findByName(EStatus.WATCHING)
                    .orElseThrow(() -> new RuntimeException("Error: Status watching not found!"));
            watchlist.setStatus(watching);
        }
        else switch(status){
            case "completed":
                Status completed = statusRepository.findByName(EStatus.COMPLETED)
                        .orElseThrow(() -> new RuntimeException("Error: Status completed not found!"));
                watchlist.setStatus(completed);
                watchlist.setProgress(series.getEpisodes());
                break;
            case "dropped":
                Status dropped = statusRepository.findByName(EStatus.DROPPED)
                        .orElseThrow(() -> new RuntimeException("Error: Status dropped not found!"));
                watchlist.setStatus(dropped);
                break;
            case "onhold":
                Status onhold = statusRepository.findByName(EStatus.ON_HOLD)
                        .orElseThrow(() -> new RuntimeException("Error: Status on hold not found!"));
                watchlist.setStatus(onhold);
                break;
            case "plantowatch":
                Status plantowatch = statusRepository.findByName(EStatus.PLAN_TO_WATCH)
                        .orElseThrow(() -> new RuntimeException("Error: Status plan to watch not found!"));
                watchlist.setStatus(plantowatch);
                watchlist.setProgress(0);
                break;
            default:
                Status watching = statusRepository.findByName(EStatus.WATCHING)
                        .orElseThrow(() -> new RuntimeException("Error: Status watching not found!"));
                watchlist.setStatus(watching);
                break;
        }
    }
}
