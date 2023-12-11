package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private TaskScheduler taskScheduler;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, TaskScheduler taskScheduler) {
        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), new ArrayList<>());
    }

    public User create(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public Page<User> paginate(Integer page, Integer size) {
        return this.userRepository.findAll(PageRequest.of(page, size, Sort.by("salary").descending()));
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    public void loggedIn(String username) {
        User user = this.userRepository.findByUsername(username).orElse(null);
        if(user == null) {
            return;
        }
//        Integer loginCount = user.getLoginCount();
//        try {
//            Thread.sleep(10000);
//
//            user.setLoginCount(loginCount + 1);
//            this.userRepository.save(user);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ObjectOptimisticLockingFailureException exception) {
//            this.loggedIn(username);
//        }
    }

//    @Scheduled(fixedDelay = 1000)
//    public void scheduleFixedDelayTask() throws InterruptedException {
//        System.out.println(
//                "Fixed delay task - " + System.currentTimeMillis() / 1000);
//        Thread.sleep(2000);
//    }

//    @Async
//    @Scheduled(fixedRate = 3000)
//    public void scheduleFixedRateTaskAsync() throws InterruptedException {
//        System.out.println(
//                "Fixed rate task async - " + System.currentTimeMillis() / 1000);
//        Thread.sleep(5000);
//        System.out.println(
//                "Fixed rate task async - finished " + System.currentTimeMillis() / 1000);
//    }
}
