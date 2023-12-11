package rs.raf.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String email;
    private Set<PermissionDto> permissions = new HashSet<>();
}
