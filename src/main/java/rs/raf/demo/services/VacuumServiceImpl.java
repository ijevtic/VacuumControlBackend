package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.mapper.VacuumMapper;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.requests.AddVacuumRequest;
import rs.raf.demo.requests.SearchRequest;
import rs.raf.demo.responses.VacuumsResponse;
import rs.raf.demo.utils.JwtUtil;
import rs.raf.demo.utils.VacuumStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static rs.raf.demo.utils.Constants.statusToName;
import static rs.raf.demo.utils.Constants.waitTime;

@Service
public class VacuumServiceImpl implements VacuumService {
    private VacuumRepository vacuumRepository;
    private UserRepository userRepository;
    private ErrorMessageService errorMessageService;
    private VacuumMapper vacuumMapper;
    private JwtUtil jwtUtil;
    @PersistenceContext
    private EntityManager entityManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public VacuumServiceImpl(VacuumRepository vacuumRepository,
                             VacuumMapper vacuumMapper,
                             UserRepository userRepository,
                             ErrorMessageService errorMessageService,
                             JwtUtil jwtUtil) {
        this.vacuumRepository = vacuumRepository;
        this.vacuumMapper = vacuumMapper;
        this.userRepository = userRepository;
        this.errorMessageService = errorMessageService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public VacuumsResponse search(SearchRequest request) {
        //get user from context
        String username = getUsernameFromJwt();
        Optional<List<Vacuum>> vs = vacuumRepository.findByUsername(username);
        if (vs.isEmpty()) {
            throw new RuntimeException("Vacuum not found");
        }

        List<VacuumDto> vacuums = vs.get().stream().map(vacuumMapper::vacuumToDto).collect(Collectors.toList());

        if(request.getName() != null) {
            vacuums = vacuums.stream().filter(vacuum -> vacuum.getName().toLowerCase().contains(request.getName().toLowerCase())).toList();
        }

        if(request.getStatus() != null) {
            vacuums = vacuums.stream().filter(vacuum ->
                    request.getStatus().contains(vacuum.getStatus())).toList();
        }

        if(request.getDateTo() != null) {
            vacuums = vacuums.stream().filter(vacuum -> vacuum.getDateCreated().compareTo(request.getDateTo()) <= 0).toList();

            if(request.getDateFrom() != null) {
                vacuums = vacuums.stream().filter(vacuum -> vacuum.getDateCreated().compareTo(request.getDateFrom()) >= 0).toList();
            }
        }

        return new VacuumsResponse(vacuums);
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
        Optional<Vacuum> vacuumOptional = this.vacuumRepository.findByName(vacuumName);
        if(vacuumOptional.isPresent()) {
            try {
                Vacuum vacuum = vacuumOptional.get();
                if(vacuum.getLocked()) {
                    return false;
                }
                vacuum.setActive(false);
                this.vacuumRepository.save(vacuum);
//                this.vacuumRepository.deleteVacuum(vacuum.getName());

            } catch (ObjectOptimisticLockingFailureException exception) {
                System.out.println("Optimistic lock exception REMOVE");
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean dischargeVacuum(String vacuumName, boolean scheduled, boolean chainCall) {
        Vacuum vacuum = vacuumRepository.findByName(vacuumName).orElse(null);

        if (vacuum == null || (!chainCall && vacuum.getLocked()) || vacuum.getStatus() != VacuumStatus.STOPPED) {
            if (scheduled)
                this.errorMessageService.add("Discharge failed", vacuum);
            return false;
        }

        try {
            vacuum.setLocked(true);
            vacuumRepository.save(vacuum);

            // Move asynchronous execution outside the method
            executorService.submit(() -> {
                try {
                    Thread.sleep(waitTime);
                    vacuum.setStatus(VacuumStatus.DISCHARGING);
                    
                    vacuumRepository.save(vacuum);

                    Thread.sleep(waitTime);
                    vacuum.setStatus(VacuumStatus.STOPPED);
                    vacuum.setLastDischarge(0);
                    vacuum.setLocked(false);
                    vacuum.setVersion(vacuum.getVersion()+1);

                    vacuumRepository.save(vacuum);

                    System.out.println("discharged " + vacuum.getName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();

                    throw new RuntimeException(e);
                }
            });

        } catch (ObjectOptimisticLockingFailureException exception) {
            if (scheduled)
                this.errorMessageService.add("Discharge failed", vacuum);
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean startVacuum(String vacuumName, boolean scheduled) {
        Vacuum vacuum = vacuumRepository.findByName(vacuumName).orElse(null);

        if (vacuum == null || vacuum.getLocked() || vacuum.getStatus() != VacuumStatus.STOPPED) {
            if (scheduled)
                this.errorMessageService.add("Start failed", vacuum);
            return false;
        }

        try {
            vacuum.setLocked(true);
            vacuumRepository.save(vacuum);

            // Move asynchronous execution outside the method
            executorService.submit(() -> {
                try {
                    Thread.sleep(waitTime);
                    vacuum.setStatus(VacuumStatus.RUNNING);
                    vacuum.setLocked(false);
                    vacuumRepository.save(vacuum);
                    System.out.println("startovao sam " + vacuum.getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (ObjectOptimisticLockingFailureException exception) {
            if (scheduled)
                this.errorMessageService.add("Start failed", vacuum);
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public boolean stopVacuum(String vacuumName, boolean scheduled) {
        Vacuum vacuum = vacuumRepository.findByName(vacuumName).orElse(null);

        if (vacuum == null || vacuum.getLocked() || vacuum.getStatus() != VacuumStatus.RUNNING) {
            if (scheduled)
                this.errorMessageService.add("Stop failed", vacuum);
            return false;
        }

        try {
            vacuum.setLocked(true);
            vacuumRepository.save(vacuum);

            // Move asynchronous execution outside the method
            executorService.submit(() -> {
                try {
                    Thread.sleep(waitTime);
                    vacuum.setStatus(VacuumStatus.STOPPED);
                    vacuum.setLastDischarge(vacuum.getLastDischarge() + 1);
                    System.out.println("stopovao sam " + vacuum.getName());

                    if(vacuum.getLastDischarge() == 3) {
                        vacuumRepository.save(vacuum);
                        this.dischargeVacuum(vacuumName, false, true);
                    } else {
                        vacuum.setLocked(false);
                        vacuumRepository.save(vacuum);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (ObjectOptimisticLockingFailureException exception) {
            if (scheduled)
                this.errorMessageService.add("Stop failed", vacuum);
            return false;
        }

        return true;
    }


    private String getUsernameFromJwt() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        System.out.println(jwtUtil.extractUsername((String)(authentication.getDetails())));
        return jwtUtil.extractUsername((String)(authentication.getDetails()));
    }
}
