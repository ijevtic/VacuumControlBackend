package rs.raf.demo.responses;

import lombok.Data;
import rs.raf.demo.dto.UserSimpleDto;
import rs.raf.demo.dto.VacuumDto;

import java.util.List;

@Data
public class VacuumsResponse {
    private List<VacuumDto> vacuums;
    public VacuumsResponse(List<VacuumDto> vacuums) {
        this.vacuums = vacuums;
    }
}
