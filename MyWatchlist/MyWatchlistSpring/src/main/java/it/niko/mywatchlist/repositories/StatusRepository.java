package it.niko.mywatchlist.repositories;

import it.niko.mywatchlist.entities.EStatus;
import it.niko.mywatchlist.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(EStatus name);
}
