package rs.raf.demo.services;

import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.requests.AddVacuumRequest;
import rs.raf.demo.requests.SearchRequest;
import rs.raf.demo.responses.VacuumsResponse;

import java.util.List;

public interface VacuumService {
    VacuumsResponse search(SearchRequest request);
    boolean add(AddVacuumRequest request);
    boolean remove(String vacuumName);
    boolean dischargeVacuum(String vacuumName, boolean scheduled, boolean chainCall);
    public boolean startVacuum(String vacuumName, boolean scheduled);
    public boolean stopVacuum(String vacuumName, boolean scheduled);

}
