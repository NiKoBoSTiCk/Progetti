package it.niko.mywatchlist.repositories;

import it.niko.mywatchlist.entities.EGenre;
import it.niko.mywatchlist.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Genre findByName(EGenre name);
    boolean existsByName(EGenre name);
}
