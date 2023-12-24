package rs.raf.demo.requests;

import lombok.Data;
import rs.raf.demo.dto.PermissionDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class AddUserRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> permissions = new ArrayList<>();
}
