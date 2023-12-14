package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.mapper.UserMapper;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.requests.LoginRequest;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public String login(LoginRequest credentials) {
        UserDto user = userRepository.findByEmail(credentials.getEmail()).map(userMapper::toDto).orElse(null);
        if (user == null) {
            //TODO exception
            return null;
//            throw new RequestNotValidException("login");
        }
        if (!user.getPassword().equals(credentials.getPassword())) {
            return null;
//            throw new RequestNotValidException("login");
        }

        return "new token";
    }
}
