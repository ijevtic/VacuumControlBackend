package rs.raf.demo.services;

import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto findByUsername(String username);
    public UserDto findByEmail(String email);
    public UserDto create(UserDto user);
    public List<UserDto> getAllUsers();
    public String login(LoginRequest credentials);
}
