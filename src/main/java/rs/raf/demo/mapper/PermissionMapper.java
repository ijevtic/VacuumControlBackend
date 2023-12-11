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
            0L, PermissionType.CREATE,
            1L, PermissionType.READ,
            2L, PermissionType.UPDATE,
            3L, PermissionType.DELETE
    );

    Map<PermissionType, Long> typeToNumber = new HashMap<>();

    {
        for (Map.Entry<Long, PermissionType> entry : numberToType.entrySet()) {
            typeToNumber.put(entry.getValue(), entry.getKey());
        }
    }

    public PermissionDto toDto(Permission permission) {
        PermissionDto dto = new PermissionDto();
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        dto.setType(numberToType.get(permission.getPermissionId()));
        return dto;
    }

    public Permission toEntity(PermissionDto dto) {
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        permission.setPermissionId(typeToNumber.get(dto.getType()));
        return permission;
    }

}
