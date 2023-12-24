package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.responses.JwtResponse;
import rs.raf.demo.services.UserServiceImpl;
import rs.raf.demo.utils.JwtUtil;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserServiceImpl userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

//    @SkipJwtFilter
    @PostMapping(value= "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest credentials) {
        System.out.printf("Login: %s\n", credentials.getEmail());
        String token = this.userService.login(credentials);
        if(token == null)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return ResponseEntity.ok(new JwtResponse(token));
    }

}
