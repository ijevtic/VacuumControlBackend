package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.Vacuum;

import java.util.List;
import java.util.Optional;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

    public Optional<List<ErrorMessage>> findAllByVacuum(Vacuum vacuum);
}
