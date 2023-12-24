package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.requests.UpdateUserRequest;
import rs.raf.demo.security.CheckSecurity;
import rs.raf.demo.security.SkipJwtFilter;
import rs.raf.demo.services.UserService;
import org.springframework.http.ResponseEntity;


import javax.validation.Valid;
import java.util.List;

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
    @PostMapping(value= "/create-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        UserDto createdUser = this.userService.create(user);
        if(createdUser == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @CheckSecurity(role = "Delete")
    @DeleteMapping(value = "/users/delete/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) {
        if (!this.userService.delete(userId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CheckSecurity(role = "Read")
    @GetMapping(value= "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> readAllUsers() {
        System.out.println("read method controller");
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @CheckSecurity(role = "Update")
    @DeleteMapping(value = "/users/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequest user) {
        if (!this.userService.update(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
