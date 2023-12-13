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
import rs.raf.demo.dto.UserCredentialsDto;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.mapper.UserMapper;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        UserDto myUser = this.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), new ArrayList<>());
    }

    public UserDto create(UserDto user) {
        if(findByEmail(user.getEmail()) != null || findByUsername(user.getUsername()) != null) {
            return null;
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        return userMapper.toDto(this.userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return this.userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public String login(UserCredentialsDto credentials) {
        Optional<User> user = this.userRepository.findByEmail(credentials.getEmail());
        if(user.isPresent()) {
            if(this.passwordEncoder.matches(credentials.getPassword(), user.get().getPassword())) {
                return "OK";
            }
        }
        return null;
    }

    public UserDto findByUsername(String username) {
        return this.userRepository.findByUsername(username).map(userMapper::toDto).orElse(null);
    }

    public UserDto findByEmail(String email) {
        return this.userRepository.findByEmail(email).map(userMapper::toDto).orElse(null);
    }
}
