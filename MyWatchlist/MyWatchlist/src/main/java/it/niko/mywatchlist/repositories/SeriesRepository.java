package it.niko.mywatchlist.repositories;


import it.niko.mywatchlist.entities.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer> {
    Page<Series> findByNameContaining(String name, Pageable pageable);
    Page<Series> findByRatingBetween(double minRating, double maxRating, Pageable pageable);
    Page<Series> findByViewsBetween(int minViews, int maxViews, Pageable pageable);
    boolean existsByName(String name);
}
