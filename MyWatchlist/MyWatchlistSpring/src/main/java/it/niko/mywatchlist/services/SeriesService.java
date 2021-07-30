package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.Genre;
import it.niko.mywatchlist.entities.Series;
import it.niko.mywatchlist.repositories.GenreRepository;
import it.niko.mywatchlist.repositories.SeriesRepository;
import it.niko.mywatchlist.support.exceptions.GenreNotExistException;
import it.niko.mywatchlist.support.exceptions.SeriesAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.SeriesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Transactional
    public void addSeries(Series series) throws SeriesAlreadyExistsException {
        if(seriesRepository.existsByTitle(series.getTitle()))
            throw new SeriesAlreadyExistsException();
        seriesRepository.save(series);
    }

    @Transactional
    public void removeSeries(Series series) throws SeriesNotFoundException {
        if(!seriesRepository.existsByTitle(series.getTitle()))
            throw new SeriesNotFoundException(series.getTitle());
        seriesRepository.delete(series);
    }

    @Transactional
    public void updateSeries(Series series) throws SeriesNotFoundException {
        if(!seriesRepository.existsByTitle(series.getTitle()))
            throw new SeriesNotFoundException(series.getTitle());

        seriesRepository.findById(series.getId()).map(ser -> {
            ser.setTitle(series.getTitle());
            ser.setEpisodes(series.getEpisodes());
            ser.setGenres(series.getGenres());
            ser.setPlot(series.getPlot());
            return seriesRepository.save(ser);
        });
    }

    @Transactional(readOnly = true)
    public List<Series> showAllSeries(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByRating(double rating, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findByRating(rating, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByViews(int minViews, int maxViews, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findByViewsBetween(minViews, maxViews, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByTitle(String title, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findByTitleContaining(title, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByGenres(Genre genre, int pageNumber, int pageSize, String sortBy) throws GenreNotExistException {
        if(!genreRepository.existsByName(genre.getName()))
            throw new GenreNotExistException(genre.getName());
        Genre gen = genreRepository.findByName(genre.getName());

        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findByGenresContains(gen, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }
}
