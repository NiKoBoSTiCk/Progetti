package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.*;
import it.niko.mywatchlist.payload.request.SeriesRequest;
import it.niko.mywatchlist.repositories.GenreRepository;
import it.niko.mywatchlist.repositories.SeriesRepository;
import it.niko.mywatchlist.repositories.WatchlistRepository;
import it.niko.mywatchlist.support.exceptions.SeriesAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.SeriesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Transactional
    public void addSeries(SeriesRequest seriesRequest) throws SeriesAlreadyExistsException {
        if(seriesRepository.existsByTitle(seriesRequest.getTitle()))
            throw new SeriesAlreadyExistsException();

        Series series = new Series(seriesRequest.getTitle());
        series.setEpisodes(seriesRequest.getEpisodes());
        series.setPlot(seriesRequest.getPlot());
        Set<String> strGenres = seriesRequest.getGenres();
        if(strGenres != null){
            Set<Genre> genres = assignGenres(strGenres);
            series.setGenres(genres);
        }
        seriesRepository.save(series);
    }

    @Transactional
    public void removeSeries(String title) throws SeriesNotFoundException {
        Series series = seriesRepository.findByTitle(title).orElseThrow(SeriesNotFoundException::new);
        List<Watchlist> toDel = watchlistRepository.findBySeries(series);
        watchlistRepository.deleteAll(toDel);
        seriesRepository.delete(series);
    }

    @Transactional
    public void updateSeries(SeriesRequest seriesRequest) throws SeriesNotFoundException {
        Series series = seriesRepository.findByTitle(seriesRequest.getTitle()).orElseThrow(SeriesNotFoundException::new);
        series.setEpisodes(seriesRequest.getEpisodes());
        series.setPlot(seriesRequest.getPlot());
        Set<String> strGenres = seriesRequest.getGenres();
        if(strGenres != null){
            Set<Genre> genres = assignGenres(strGenres);
            series.setGenres(genres);
        }
        seriesRepository.save(series);
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
    public List<Series> showSeriesByGenres(String genre, int pageNumber, int pageSize, String sortBy){
        Set<String> strGenres = new HashSet<>();
        strGenres.add(genre);
        Set<Genre> genres = assignGenres(strGenres);
        Genre gen = genres.stream().findFirst().orElseThrow(() -> new RuntimeException("Error: Genre not found."));

        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findByGenresContains(gen, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    private Set<Genre> assignGenres(Set<String> strGenres){
        Set<Genre> genres = new HashSet<>();
        strGenres.forEach(genre -> {
            switch(genre) {
                case "drama":
                    Genre drama = genreRepository.findByName(EGenre.DRAMA)
                            .orElseThrow(() -> new RuntimeException("Error: Genre drama not found."));
                    genres.add(drama);
                    break;
                case "crime":
                    Genre crime = genreRepository.findByName(EGenre.CRIME)
                            .orElseThrow(() -> new RuntimeException("Error: Genre crime not found."));
                    genres.add(crime);
                    break;
                case "comedy":
                    Genre comedy = genreRepository.findByName(EGenre.COMEDY)
                            .orElseThrow(() -> new RuntimeException("Error: Genre comedy not found."));
                    genres.add(comedy);
                    break;
                case "horror":
                    Genre horror = genreRepository.findByName(EGenre.HORROR)
                            .orElseThrow(() -> new RuntimeException("Error: Genre horror not found."));
                    genres.add(horror);
                    break;
                case "fantasy":
                    Genre fantasy = genreRepository.findByName(EGenre.FANTASY)
                            .orElseThrow(() -> new RuntimeException("Error: Genre fantasy not found."));
                    genres.add(fantasy);
                    break;
                case "thriller":
                    Genre thriller = genreRepository.findByName(EGenre.THRILLER)
                            .orElseThrow(() -> new RuntimeException("Error: Genre thriller not found."));
                    genres.add(thriller);
                    break;
                case "action":
                    Genre action = genreRepository.findByName(EGenre.ACTION)
                            .orElseThrow(() -> new RuntimeException("Error: Genre action not found."));
                    genres.add(action);
                    break;
                case "adventure":
                    Genre adventure = genreRepository.findByName(EGenre.ADVENTURE)
                            .orElseThrow(() -> new RuntimeException("Error: Genre adventure not found."));
                    genres.add(adventure);
                    break;
                default: throw new RuntimeException("Error: Genre not found.");
            }
        });
        return genres;
    }
}
