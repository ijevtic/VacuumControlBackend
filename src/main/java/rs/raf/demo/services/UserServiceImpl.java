package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.mapper.PermissionMapper;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.mapper.UserMapper;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.requests.UpdateUserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PermissionMapper permissionMapper;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper, PermissionMapper permissionMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
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
    public String login(LoginRequest credentials) {
        Optional<User> user = this.userRepository.findByEmail(credentials.getEmail());
        if(user.isPresent()) {
            if(this.passwordEncoder.matches(credentials.getPassword(), user.get().getPassword())) {
                return "OK";
            }
        }
        return null;
    }

    @Override
    public boolean delete(Long userId) {
        if(this.userRepository.findByUserId(userId).isPresent()) {
            return false;
        }

        this.userRepository.deleteByUserId(userId);
        return true;
    }

    @Override
    public boolean update(UpdateUserRequest user) {
        User userToUpdate = this.userRepository.findByUsername(user.getUsername()).orElse(null);

        if(userToUpdate == null) {
            return false;
        }

        if(user.getPermissions() != null) {
            userToUpdate.setPermissions(user.getPermissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()));
        }

        if(user.getPassword() != null) {
            userToUpdate.setPassword(this.passwordEncoder.encode(user.getPassword()));
        }

        if(user.getFirstName() != null) {
            userToUpdate.setFirstName(user.getFirstName());
        }

        if(user.getLastName() != null)
            userToUpdate.setLastName(user.getLastName());

        this.userRepository.save(userToUpdate);
        return true;
    }

    public UserDto findByUsername(String username) {
        return this.userRepository.findByUsername(username).map(userMapper::toDto).orElse(null);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto myUser = this.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), new ArrayList<>());
    }


    public UserDto findByEmail(String email) {
        return this.userRepository.findByEmail(email).map(userMapper::toDto).orElse(null);
    }
}
