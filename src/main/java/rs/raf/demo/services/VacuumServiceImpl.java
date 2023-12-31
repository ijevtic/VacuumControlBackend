package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.mapper.VacuumMapper;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.requests.AddVacuumRequest;
import rs.raf.demo.requests.SearchRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static rs.raf.demo.utils.Constants.statusToName;

@Service
public class VacuumServiceImpl implements VacuumService {
    private VacuumRepository vacuumRepository;
    private UserRepository userRepository;
    private ErrorMessageService errorMessageService;
    private VacuumMapper vacuumMapper;

    @Autowired
    public VacuumServiceImpl(VacuumRepository vacuumRepository, VacuumMapper vacuumMapper, UserRepository userRepository, ErrorMessageService errorMessageService) {
        this.vacuumRepository = vacuumRepository;
        this.vacuumMapper = vacuumMapper;
        this.userRepository = userRepository;
        this.errorMessageService = errorMessageService;
    }

    @Override
    public List<VacuumDto> search(SearchRequest request) {
        //get user from context
        String username = getUsernameFromJwt();
        List<VacuumDto> vacuums = vacuumRepository.findByUsername(username).
                orElseThrow(() -> new RuntimeException("Vacuum not found")).stream().map(vacuumMapper::vacuumToDto).collect(Collectors.toList());

        if(request.getName() != null) {
            vacuums = vacuums.stream().filter(vacuum -> vacuum.getName().toLowerCase().contains(request.getName().toLowerCase())).toList();
        }

        if(request.getStatus() != null) {
            vacuums = vacuums.stream().filter(vacuum ->
                    request.getStatus().contains(statusToName.get(vacuum.getStatus()))).toList();
        }

        if(request.getDateTo() != null) {
            vacuums = vacuums.stream().filter(vacuum -> vacuum.getDateCreated().compareTo(request.getDateTo()) <= 0).toList();

            if(request.getDateFrom() != null) {
                vacuums = vacuums.stream().filter(vacuum -> vacuum.getDateCreated().compareTo(request.getDateFrom()) >= 0).toList();
            }
        }

        return vacuums;
    }

    @Override
    public boolean add(AddVacuumRequest request) {
        //get user from context
        Optional<Vacuum> vacuumOptional = this.vacuumRepository.findByName(request.getName());
        if(vacuumOptional.isPresent())
            return false;

        String username = getUsernameFromJwt();
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Vacuum vacuum = this.vacuumMapper.requestToVacuum(request, user);
        this.vacuumRepository.save(vacuum);
        return true;
    }

    @Override
    public boolean remove(String vacuumName) {
        return false;
    }

    @Override
    public boolean discharge(String vacuumName) {
        Vacuum vacuum = this.vacuumRepository.findByName(vacuumName).orElseThrow(() -> new RuntimeException("Vacuum not found"));
        this.errorMessageService.add("Discharge failed", vacuum);
        return false;
    }

    @Override
    public boolean startVacuum(String vacuumName) {
        Vacuum vacuum = this.vacuumRepository.findByName(vacuumName).orElseThrow(() -> new RuntimeException("Vacuum not found"));
        this.errorMessageService.add("Start failed", vacuum);
        return false;
    }

    @Override
    public boolean stopVacuum(String vacuumName) {
        Vacuum vacuum = this.vacuumRepository.findByName(vacuumName).orElseThrow(() -> new RuntimeException("Vacuum not found"));
        this.errorMessageService.add("Stop failed", vacuum);
        return false;
    }


    private String getUsernameFromJwt() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails)authentication.getDetails()).getUsername();
    }
}
