package rs.raf.demo.dto;

import lombok.Getter;
import lombok.Setter;
import rs.raf.demo.utils.PermissionType;

@Getter
@Setter
public class PermissionDto {
    PermissionType type;
    String name;
    String description;
}
