package rs.raf.demo.dto;

import lombok.Data;
import rs.raf.demo.utils.VacuumStatus;

@Data
public class VacuumDto {
    private String name;
    private VacuumStatus status;
    private Boolean active;
    private Long dateCreated;
}
