package it.niko.mywatchlist.repositories;

import it.niko.mywatchlist.entities.Genre;
import it.niko.mywatchlist.entities.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer> {
    Page<Series> findByTitleContaining(String title, Pageable pageable);
    Page<Series> findByRating(double rating, Pageable pageable);
    Page<Series> findByViewsBetween(int minViews, int maxViews, Pageable pageable);
    Page<Series> findByGenres(Set<Genre> genres, Pageable pageable);
    boolean existsByTitle(String title);
}
