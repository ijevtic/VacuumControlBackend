package rs.raf.demo.mapper;

import org.springframework.stereotype.Component;
import rs.raf.demo.dto.PermissionDto;
import rs.raf.demo.model.Permission;
import rs.raf.demo.utils.Constants;
import rs.raf.demo.utils.PermissionType;

import java.util.HashMap;
import java.util.Map;

import static rs.raf.demo.utils.Constants.*;

@Component
public class PermissionMapper {

    public PermissionDto toDto(Permission permission) {
        PermissionDto dto = new PermissionDto();
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        dto.setType(numberToType.get(permission.getPermissionId()));
        return dto;
    }

    public String toString(Permission permission) {
        return permission.getName();
    }

    public Permission toEntity(String permission) {
        Permission p = new Permission();
        p.setName(permission);
        p.setPermissionId(nameToNumber.get(permission));
        System.out.println(p.getPermissionId());
        return p;
    }

    public Permission toEntity(PermissionDto dto) {
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        permission.setPermissionId(typeToNumber.get(dto.getType()));
        return permission;
    }

}
