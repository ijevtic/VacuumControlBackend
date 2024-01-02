package rs.raf.demo.mapper;

import org.springframework.stereotype.Component;
import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.requests.AddVacuumRequest;
import rs.raf.demo.utils.VacuumStatus;

import static rs.raf.demo.utils.Constants.statusToName;

@Component
public class VacuumMapper {
    public VacuumDto vacuumToDto(Vacuum vacuum) {
        VacuumDto dto = new VacuumDto();
        dto.setName(vacuum.getName());
        dto.setStatus(statusToName.get(vacuum.getStatus()));
        dto.setDateCreated(vacuum.getDateCreated());
        
        return dto;
    }

    public Vacuum requestToVacuum(AddVacuumRequest request, User user) {
        Vacuum vacuum = new Vacuum();
        vacuum.setName(request.getName());
        vacuum.setActive(true);
        vacuum.setStatus(VacuumStatus.STOPPED);
        vacuum.setDateCreated(System.currentTimeMillis() / 1000L);
        vacuum.setLocked(false);
        vacuum.setUser(user);
        return vacuum;
    }
}
