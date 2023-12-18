package rs.raf.demo.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import rs.raf.demo.dto.PermissionDto;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UpdateUserRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Set<PermissionDto> permissions = new HashSet<>();
}
