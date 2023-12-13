package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    public Optional<Permission> findByName(String name);

//    @Modifying
//    @Query("update User u set u.balance = u.balance + :amount")
//    @Transactional
//    public void increaseBalance(@Param("amount") Integer amount);
}

