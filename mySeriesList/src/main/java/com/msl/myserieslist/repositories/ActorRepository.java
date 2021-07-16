package com.msl.myserieslist.repositories;

import com.msl.myserieslist.entities.Actor;
import com.msl.myserieslist.entities.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    List<Actor> findByFirstName(String firstName);
    List<Actor> findByLastName(String lastName);
    List<Actor> findByCareerContains(Series series);
}
