package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.Series;
import it.niko.mywatchlist.entities.SeriesInList;
import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.repositories.SeriesInListRepository;
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
public class SeriesInListService {
    @Autowired
    private SeriesInListRepository seriesInListRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addSeriesInList(SeriesInList seriesInList) throws SeriesAlreadyInWatchlistException, ProgressNotValidException, UserNotFoundException, SeriesNotFoundException, ScoreNotValidException {
        if(seriesInListRepository.existsById(seriesInList.getId()))
            throw new SeriesAlreadyInWatchlistException();
        if(!userRepository.existsById(seriesInList.getUser().getId()))
            throw new UserNotFoundException(seriesInList.getUser().getUsername());
        User user = userRepository.getById(seriesInList.getUser().getId());
        if(!seriesRepository.existsById(seriesInList.getSeries().getId()))
            throw new SeriesNotFoundException(seriesInList.getSeries().getTitle());
        Series series = seriesRepository.getById(seriesInList.getSeries().getId());
        if(seriesInListRepository.existsByUserAndSeries(user, series))
            throw new SeriesAlreadyInWatchlistException();
        series.upgradeViews();
        if(seriesInList.getProgress() < 0 ||  seriesInList.getProgress() > seriesInList.getSeries().getEpisodes())
            throw new ProgressNotValidException(seriesInList.getProgress());
        if(seriesInList.getScore() < 0 || seriesInList.getScore() > 10)
            throw new ScoreNotValidException(seriesInList.getScore());
        if(seriesInList.getScore() != 0)
            series.updateRating(seriesInList.getScore(), false);
        if(seriesInList.getSeries().getEpisodes() == seriesInList.getProgress())
            seriesInList.setStatus(SeriesInList.Status.COMPLETED);

        seriesInListRepository.save(seriesInList);
    }

    @Transactional
    public void updateSeriesInList(SeriesInList seriesInList) throws SeriesNotInWatchlistException, ProgressNotValidException, ScoreNotValidException, UserNotFoundException, SeriesNotFoundException {
        if(!seriesInListRepository.existsById(seriesInList.getId()))
            throw new SeriesNotInWatchlistException();
        if(!userRepository.existsById(seriesInList.getUser().getId()))
            throw new UserNotFoundException(seriesInList.getUser().getUsername());
        if(!seriesRepository.existsById(seriesInList.getSeries().getId()))
            throw new SeriesNotFoundException(seriesInList.getSeries().getTitle());
        Series series = seriesRepository.getById(seriesInList.getSeries().getId());
        if(seriesInList.getProgress() < 0 ||  seriesInList.getProgress() > seriesInList.getSeries().getEpisodes())
            throw new ProgressNotValidException(seriesInList.getProgress());
        if(seriesInList.getScore() < 0 || seriesInList.getScore() > 10)
            throw new ScoreNotValidException(seriesInList.getScore());
        if(seriesInList.getScore() != 0)
            series.updateRating(seriesInList.getScore(), false);

        seriesInListRepository.findById(seriesInList.getId()).map(sil -> {
            sil.setComment(seriesInList.getComment());
            sil.setScore(seriesInList.getScore());
            sil.setProgress(seriesInList.getProgress());
            sil.setStatus(seriesInList.getStatus());
            if(seriesInList.getSeries().getEpisodes() == sil.getProgress())
                sil.setStatus(SeriesInList.Status.COMPLETED);
            return seriesInListRepository.save(sil);
        });
    }

    @Transactional
    public void removeSeriesInList(SeriesInList seriesInList) throws SeriesNotInWatchlistException, UserNotFoundException, SeriesNotFoundException {
        if(!seriesInListRepository.existsById(seriesInList.getId()))
            throw new SeriesNotInWatchlistException();
        if(!userRepository.existsById(seriesInList.getUser().getId()))
            throw new UserNotFoundException(seriesInList.getUser().getUsername());
        if(!seriesRepository.existsById(seriesInList.getSeries().getId()))
            throw new SeriesNotFoundException(seriesInList.getSeries().getTitle());
        if(seriesInList.getScore() != 0){
            Series series = seriesRepository.getById(seriesInList.getSeries().getId());
            series.updateRating(seriesInList.getScore(), true);
        }
        seriesInListRepository.delete(seriesInList);
    }

    @Transactional(readOnly = true)
    public List<SeriesInList> showAllSeriesInList(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<SeriesInList> pagedResult = seriesInListRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<SeriesInList> showUserWatchlist(User user, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<SeriesInList> pagedResult = seriesInListRepository.findByUser(user, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<SeriesInList> showUserWatchlist(User user, SeriesInList.Status status , int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<SeriesInList> pagedResult = seriesInListRepository.findByUserAndStatus(user, status, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<SeriesInList> showUserWatchlist(User user, int score , int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<SeriesInList> pagedResult = seriesInListRepository.findByUserAndScoreLessThanEqual(user, score, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }
}
