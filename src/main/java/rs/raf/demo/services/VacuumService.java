package rs.raf.demo.services;

import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.requests.SearchRequest;

import java.util.List;

public interface VacuumService {
    List<VacuumDto> search(SearchRequest request);
}
