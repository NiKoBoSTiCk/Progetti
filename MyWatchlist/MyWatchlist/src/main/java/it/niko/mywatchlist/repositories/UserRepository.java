package it.niko.mywatchlist.repositories;

import it.niko.mywatchlist.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findByNicknameContaining(String nickname, Pageable pageable);
    User findByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
