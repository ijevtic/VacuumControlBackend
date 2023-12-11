package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.mapper.UserMapper;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), new ArrayList<>());
    }

    public UserDto create(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userMapper.toDto(this.userRepository.save(user));
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }
}
