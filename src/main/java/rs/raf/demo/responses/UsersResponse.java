package rs.raf.demo.responses;

import lombok.Getter;
import lombok.Setter;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.dto.UserSimpleDto;

import java.util.List;

@Getter
@Setter
public class UsersResponse {
    private List<UserSimpleDto> users;
    public UsersResponse(List<UserSimpleDto> users) {
        this.users = users;
    }
}
