package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(this.passwordEncoder.encode("user1"));
        user1.setEmail("user1@user.com");
        user1.setFirstName("Name1");
        user1.setLastName("LastName1");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(this.passwordEncoder.encode("user2"));
        user2.setEmail("user2@user.com");
        user2.setFirstName("Name2");
        user2.setLastName("LastName2");

        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword(this.passwordEncoder.encode("user3"));
        user3.setEmail("user3@user.com");
        user3.setFirstName("Name3");
        user3.setLastName("LastName3");

        Permission p1 = new Permission();
        p1.setName("Create");
        p1.setDescription("Create permission");
        p1.setPermissionId(1L);

        Permission p2 = new Permission();
        p2.setName("Read");
        p2.setDescription("Read permission");
        p2.setPermissionId(2L);

        Permission p3 = new Permission();
        p3.setName("Update");
        p3.setDescription("Update permission");
        p3.setPermissionId(3L);

        Permission p4 = new Permission();
        p4.setName("Delete");
        p4.setDescription("Delete permission");
        p4.setPermissionId(4L);

        this.permissionRepository.save(p1);
        this.permissionRepository.save(p2);
        this.permissionRepository.save(p3);
        this.permissionRepository.save(p4);

        user1.setPermissions(Set.of(p1, p2, p3, p4));
        this.userRepository.save(user1);

        user2.setPermissions(Set.of(p1, p2, p3));
        this.userRepository.save(user2);

//        user3.setPermissions(Set.of(p1, p2));
        this.userRepository.save(user3);

        System.out.println("Data loaded!");
    }
}
