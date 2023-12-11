package rs.raf.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.model.User;

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
        dto.setEmail(user.getEmail());
        dto.setPermissions(user.getPermissions().stream().map(permissionMapper::toDto).collect(Collectors.toSet()));
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setPermissions(dto.getPermissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()));
        return user;
    }
}
