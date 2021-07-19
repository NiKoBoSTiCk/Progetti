package com.msl.myserieslist.repositories;

import com.msl.myserieslist.entities.Actor;
import com.msl.myserieslist.entities.Series;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer> {
    List<Series> findByName(String name);
    List<Series> findByNameContaining(String name);
    List<Series> findByCastContaining(Actor actor);
    boolean existsByName(String name);
}
