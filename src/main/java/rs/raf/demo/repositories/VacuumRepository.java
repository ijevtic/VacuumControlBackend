package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Vacuum;

import java.util.List;
import java.util.Optional;


@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {
//    public Optional<Vacuum> findByAddedBy(Long id);

    @Query("select v from Vacuum v where v.user.username = :username and v.active = true")
//    @Transactional
    public Optional<List<Vacuum>> findByUsername(@Param("username") String username);

    @Query("select v from Vacuum v where v.name = :name and v.active = true")
    public Optional<Vacuum> findByName(String name);

    @Query("SELECT v FROM Vacuum v WHERE v.vacuumId = :id AND v.active = true")
    Optional<Vacuum> getReferenceByVacuumId(Long id);

    @Query("SELECT v FROM Vacuum v WHERE v.name = :name AND v.active = true")
    Optional<Vacuum> getReferenceByName(String name);

    @Query("update Vacuum v set v.active = false where v.name = :vacuumName")
    @Modifying
    public void deleteVacuum(@Param("vacuumName") String vacuumName);
}
