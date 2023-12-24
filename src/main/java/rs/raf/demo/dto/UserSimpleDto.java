package rs.raf.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserSimpleDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> permissions = new ArrayList<>();
}
