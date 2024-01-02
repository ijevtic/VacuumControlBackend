package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.raf.demo.mapper.ErrorMessageMapper;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.responses.ErrorsResponse;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ErrorMessageServiceImpl implements ErrorMessageService{
    private ErrorMessageMapper errorMessageMapper;
    private ErrorMessageRepository errorMessageRepository;
    private VacuumRepository vacuumRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public ErrorMessageServiceImpl(ErrorMessageMapper errorMessageMapper,
                                   ErrorMessageRepository errorMessageRepository,
                                   VacuumRepository vacuumRepository,
                                   JwtUtil jwtUtil) {
        this.errorMessageMapper = errorMessageMapper;
        this.errorMessageRepository = errorMessageRepository;
        this.vacuumRepository = vacuumRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean add(String message, Vacuum vacuum) {
        ErrorMessage errorMessage = this.errorMessageMapper.toEntity(message, vacuum);
        this.errorMessageRepository.save(errorMessage);
        return true;
    }

    @Override
    public ErrorsResponse get() {
        String username = this.getUsernameFromJwt();
        List<ErrorMessage> errorMessages = this.errorMessageRepository.findAllByUsername(username).orElse(null);
        if(errorMessages == null) {
            return new ErrorsResponse(null);
        }
        return new ErrorsResponse(errorMessages.stream().map(this.errorMessageMapper::toDto).collect(Collectors.toList()));
    }

    private String getUsernameFromJwt() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        System.out.println(jwtUtil.extractUsername((String)(authentication.getDetails())));
        return jwtUtil.extractUsername((String)(authentication.getDetails()));
    }


}
