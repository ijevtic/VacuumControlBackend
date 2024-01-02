package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.ErrorMessage;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

    @Query("SELECT e FROM ErrorMessage e JOIN e.vacuum v JOIN v.user u WHERE u.username = :username")
    public Optional<List<ErrorMessage>> findAllByUsername(@Param("username") String username);
}
