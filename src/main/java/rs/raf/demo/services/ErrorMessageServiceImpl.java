package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.mapper.ErrorMessageMapper;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.VacuumRepository;

import java.util.List;

@Service
public class ErrorMessageServiceImpl implements ErrorMessageService{
    private ErrorMessageMapper errorMessageMapper;
    private ErrorMessageRepository errorMessageRepository;
    private VacuumRepository vacuumRepository;

    @Autowired
    public ErrorMessageServiceImpl(ErrorMessageMapper errorMessageMapper,
                                   ErrorMessageRepository errorMessageRepository,
                                   VacuumRepository vacuumRepository) {
        this.errorMessageMapper = errorMessageMapper;
        this.errorMessageRepository = errorMessageRepository;
        this.vacuumRepository = vacuumRepository;
    }

    @Override
    public boolean add(String message, Vacuum vacuum) {
        ErrorMessage errorMessage = this.errorMessageMapper.toEntity(message, vacuum);
        this.errorMessageRepository.save(errorMessage);
        return true;
    }

    @Override
    public List<ErrorMessage> get(Vacuum vacuum) {
        return this.errorMessageRepository.
                findAllByVacuum(vacuum).orElse(null);
    }
}
