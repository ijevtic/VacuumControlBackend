package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.dto.UserSimpleDto;
import rs.raf.demo.mapper.PermissionMapper;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.mapper.UserMapper;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.requests.UpdateUserRequest;
import rs.raf.demo.utils.JwtUtil;

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
    private JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper,
                           PermissionMapper permissionMapper, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.jwtUtil = jwtUtil;
    }

    public UserDto create(UserDto user) {
        if(findByEmail(user.getEmail()) != null || findByUsername(user.getUsername()) != null) {
            return null;
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        return userMapper.toDto(this.userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public List<UserSimpleDto> getAllUsers() {
        return this.userRepository.findAll().stream().map(userMapper::toSimpleDto).collect(Collectors.toList());
    }

    @Override
    public String login(LoginRequest credentials) {
        User user = this.userRepository.findByEmail(credentials.getEmail()).orElse(null);
        if(user == null) return null;
        if(!this.passwordEncoder.matches(credentials.getPassword(), user.getPassword())) return null;

        System.out.println("User found: " + user.getUsername());

        System.out.println(user.getPermissions());

        return jwtUtil.generateToken(user);
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
        User userToUpdate = this.userRepository.findByEmail(user.getEmail()).orElse(null);


        if(userToUpdate == null) {
            return false;
        }

        if(user.getPermissions() != null) {
            userToUpdate.setPermissions(user.getPermissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()));
        }

        if(user.getPassword() != null && !user.getPassword().isEmpty()) {
            userToUpdate.setPassword(this.passwordEncoder.encode(user.getPassword()));
        }

        if(user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            userToUpdate.setFirstName(user.getFirstName());
        }

        if(user.getLastName() != null && !user.getLastName().isEmpty())
            userToUpdate.setLastName(user.getLastName());
        System.out.println(userToUpdate.getPermissions().iterator().next().getPermissionId());
        System.out.println("aaaa");
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
