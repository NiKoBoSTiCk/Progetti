package it.niko.mywatchlist.repositories;

import it.niko.mywatchlist.entities.Series;
import it.niko.mywatchlist.entities.Status;
import it.niko.mywatchlist.entities.User;
import it.niko.mywatchlist.entities.Watchlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {
    List<Watchlist> findBySeries(Series series);
    Optional<Watchlist> findByUserAndSeries(User user, Series series);
    Page<Watchlist> findByUser(User user, Pageable pageable);
    Page<Watchlist> findByUserAndStatus(User user, Status status, Pageable pageable);
    Page<Watchlist> findByUserAndScoreLessThanEqual(User user, int score, Pageable pageable);
    boolean existsByUserAndSeries(User user, Series series);
}
