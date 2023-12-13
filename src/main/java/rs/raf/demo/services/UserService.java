package rs.raf.demo.services;

import rs.raf.demo.dto.UserCredentialsDto;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.model.User;

import java.util.List;

public interface UserService {
    public UserDto findByUsername(String username);
    public UserDto create(UserDto user);
    public List<UserDto> getAllUsers();
    public String login(UserCredentialsDto credentials);
}
