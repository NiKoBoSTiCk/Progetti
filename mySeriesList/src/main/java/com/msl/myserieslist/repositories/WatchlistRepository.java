package com.msl.myserieslist.repositories;

import com.msl.myserieslist.entities.User;
import com.msl.myserieslist.entities.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {
    List<Watchlist> findByUser(User user);
    List<Watchlist> findByUserAndStatus(User user, Watchlist.Status status);
    List<Watchlist> findByUserAndScore(User user, int score);
    boolean existsById(int id);
}
