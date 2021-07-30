package it.niko.mywatchlist.repositories;

import it.niko.mywatchlist.entities.EStatus;
import it.niko.mywatchlist.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByName(EStatus name);
}
