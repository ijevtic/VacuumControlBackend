package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.UserCredentialsDto;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.model.User;
import rs.raf.demo.security.CheckSecurity;
import rs.raf.demo.security.SkipJwtFilter;
import rs.raf.demo.services.UserService;
import org.springframework.http.ResponseEntity;


import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CheckSecurity(role = "Create")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto user) {
        UserDto createdUser = this.userService.create(user);
        if(createdUser == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @CheckSecurity(role = "Read")
    @GetMapping(value= "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> readAllUsers(@RequestParam("username") String username) {
        return new ResponseEntity<>(this.userService.findByUsername(username), HttpStatus.OK);
    }

    @SkipJwtFilter
    @PostMapping(value= "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@Valid @RequestBody UserCredentialsDto credentials) {
        String token = this.userService.login(credentials);
        if(token == null)
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }



//    @GetMapping
//    public Page<User> all(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
//        return this.userService.paginate(page, size);
//    }

//    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
//    public User me() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return this.userService.findByUsername(username);
//    }

    @PostMapping(value = "/hire", produces = MediaType.APPLICATION_JSON_VALUE)
    public User hire(@RequestParam("salary") Integer salary) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return this.userService.hire(username, salary);
        return null;
    }
}
