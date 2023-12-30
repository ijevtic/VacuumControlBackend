package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;
import rs.raf.demo.utils.Constants;

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

        Permission [] ps = new Permission[10];

        for(int i = 0; i < 10; i++) {
            ps[i] = new Permission();
            ps[i].setName(Constants.permissions[i].getFirst());
            ps[i].setDescription(Constants.permissions[i].getFirst() + " description");
            ps[i].setPermissionId(Constants.permissions[i].getSecond());
            this.permissionRepository.save(ps[i]);
        }

        user1.setPermissions(Set.of(ps[0], ps[1], ps[2], ps[3], ps[4], ps[5], ps[6], ps[7], ps[8], ps[9]));
        this.userRepository.save(user1);

        user2.setPermissions(Set.of(ps[0], ps[1], ps[2]));
        this.userRepository.save(user2);

//        user3.setPermissions(Set.of(p1, p2));
        this.userRepository.save(user3);

        System.out.println("Data loaded!");
    }
}
