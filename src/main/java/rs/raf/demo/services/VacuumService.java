package rs.raf.demo.services;

import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.requests.AddVacuumRequest;
import rs.raf.demo.requests.SearchRequest;

import java.util.List;

public interface VacuumService {
    List<VacuumDto> search(SearchRequest request);
    boolean add(AddVacuumRequest request);
    boolean remove(String vacuumName);
    boolean discharge(String vacuumName);
    public boolean startVacuum(String vacuumName);
    public boolean stopVacuum(String vacuumName);

}
