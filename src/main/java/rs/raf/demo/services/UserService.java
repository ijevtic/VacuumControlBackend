package rs.raf.demo.services;

import rs.raf.demo.dto.UserDto;
import rs.raf.demo.model.User;

public interface UserService {
    public User findByUsername(String username);
    public UserDto create(User user);
}
