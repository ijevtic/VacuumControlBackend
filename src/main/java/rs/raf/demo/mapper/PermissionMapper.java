package rs.raf.demo.mapper;

import org.springframework.stereotype.Component;
import rs.raf.demo.dto.PermissionDto;
import rs.raf.demo.model.Permission;
import rs.raf.demo.utils.PermissionType;

import java.util.HashMap;
import java.util.Map;

@Component
public class PermissionMapper {
    Map<Long, PermissionType> numberToType = Map.of(
            1L, PermissionType.CREATE,
            2L, PermissionType.READ,
            3L, PermissionType.UPDATE,
            4L, PermissionType.DELETE
    );

    Map<PermissionType, Long> typeToNumber = new HashMap<>();

    {
        for (Map.Entry<Long, PermissionType> entry : numberToType.entrySet()) {
            typeToNumber.put(entry.getValue(), entry.getKey());
        }
    }

    Map<String, Long> nameToNumber = Map.of(
            "Create", 1L,
            "Read", 2L,
            "Update", 3L,
            "Delete", 4L
    );

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
