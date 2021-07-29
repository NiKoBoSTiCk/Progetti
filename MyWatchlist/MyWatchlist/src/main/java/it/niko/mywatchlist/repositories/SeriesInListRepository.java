package it.niko.mywatchlist.repositories;

import it.niko.mywatchlist.entities.Series;
import it.niko.mywatchlist.entities.SeriesInList;
import it.niko.mywatchlist.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesInListRepository extends JpaRepository<SeriesInList, Integer> {
    Page<SeriesInList> findByUser(User user, Pageable pageable);
    Page<SeriesInList> findByUserAndStatus(User user, SeriesInList.Status status, Pageable pageable);
    Page<SeriesInList> findByUserAndScoreLessThanEqual(User user, int score, Pageable pageable);
    boolean existsByUserAndSeries(User user, Series series);
}
