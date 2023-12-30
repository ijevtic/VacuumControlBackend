package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Vacuum;

import java.util.List;
import java.util.Optional;


@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {
//    public Optional<Vacuum> findByAddedBy(Long id);

    @Query("select v from Vacuum v where v.user = :username")
//    @Transactional
    public Optional<List<Vacuum>> findByUsername(@Param("username") String username);
}
