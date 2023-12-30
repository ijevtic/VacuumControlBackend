package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.requests.AddUserRequest;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.requests.UpdateUserRequest;
import rs.raf.demo.responses.UsersResponse;
import rs.raf.demo.security.CheckSecurity;
import rs.raf.demo.security.SkipJwtFilter;
import rs.raf.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import rs.raf.demo.utils.Constants;
import rs.raf.demo.utils.PermissionType;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static rs.raf.demo.utils.Constants.*;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CheckSecurity(role = CREATE)
    @PostMapping(value= "/create-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody AddUserRequest user) {
        UserDto createdUser = this.userService.create(user);
        if(createdUser == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @CheckSecurity(role = DELETE)
    @DeleteMapping(value = "/delete/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> deleteUser(@PathVariable String email) {
        if (!this.userService.delete(email)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CheckSecurity(role = READ)
    @GetMapping(value= "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersResponse> readAllUsers() {
        System.out.println("read method controller");
        return new ResponseEntity<>(new UsersResponse(this.userService.getAllUsers()), HttpStatus.OK);
    }

    @CheckSecurity(role = UPDATE)
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequest user) {
        if (!this.userService.update(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
