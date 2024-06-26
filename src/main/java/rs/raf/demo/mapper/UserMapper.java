package rs.raf.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.dto.UserSimpleDto;
import rs.raf.demo.model.User;
import rs.raf.demo.requests.AddUserRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private PermissionMapper permissionMapper;

    @Autowired
    public UserMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPermissions(user.getPermissions().stream().map(permissionMapper::toDto).collect(Collectors.toSet()));
        return dto;
    }

    public UserSimpleDto toSimpleDto(User user) {
        UserSimpleDto dto = new UserSimpleDto();
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPermissions(user.getPermissions().stream().map(permissionMapper::toString).collect(Collectors.toList()));
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPermissions(dto.getPermissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()));
        return user;
    }

    public User toEntity(AddUserRequest user) {
        User u = new User();
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        u.setPermissions(user.getPermissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()));
        return u;
    }
}
