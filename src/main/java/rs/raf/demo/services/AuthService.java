package rs.raf.demo.services;

import rs.raf.demo.dto.UserDto;
import rs.raf.demo.requests.LoginRequest;

import java.util.List;

public interface AuthService {
    public String login(LoginRequest credentials);
}

