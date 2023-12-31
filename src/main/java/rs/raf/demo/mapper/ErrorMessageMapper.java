package rs.raf.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.demo.dto.ErrorMessageDto;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.VacuumRepository;

@Component
public class ErrorMessageMapper {
    private VacuumRepository vacuumRepository;

    @Autowired
    public ErrorMessageMapper(VacuumRepository vacuumRepository) {
        this.vacuumRepository = vacuumRepository;
    }
    public ErrorMessageDto toDto(ErrorMessage errorMessage) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto();
        errorMessageDto.setMessage(errorMessage.getMessage());
        errorMessageDto.setDateCreated(errorMessage.getDateCreated());
        errorMessageDto.setVacuum(errorMessage.getVacuum());
        return errorMessageDto;
    }

    public ErrorMessage toEntity(ErrorMessageDto errorMessageDto) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(errorMessageDto.getMessage());
        errorMessage.setDateCreated(errorMessageDto.getDateCreated());
        errorMessage.setVacuum(errorMessageDto.getVacuum());
        return errorMessage;
    }

    public ErrorMessage toEntity(String message, Vacuum vacuum) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(message);
        errorMessage.setDateCreated(System.currentTimeMillis() / 1000L);
        errorMessage.setVacuum(vacuum);

        return errorMessage;
    }
}
