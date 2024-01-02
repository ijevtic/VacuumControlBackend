package rs.raf.demo.dto;

import lombok.Data;

@Data
public class VacuumDto {
    private String name;
    private String status;
    private Boolean active;
    private Long dateCreated;
}
