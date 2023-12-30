package rs.raf.demo.mapper;

import org.springframework.stereotype.Component;
import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.model.Vacuum;

@Component
public class VacuumMapper {
    public VacuumDto vacuumToDto(Vacuum vacuum) {
        VacuumDto dto = new VacuumDto();
        dto.setName(vacuum.getName());
        dto.setActive(vacuum.getActive());
        dto.setStatus(vacuum.getStatus());
        dto.setDateCreated(vacuum.getDateCreated());
        
        return dto;
    }
}
