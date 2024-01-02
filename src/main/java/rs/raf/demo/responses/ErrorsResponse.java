package rs.raf.demo.responses;

import lombok.Data;
import rs.raf.demo.dto.ErrorMessageDto;

import java.util.List;

@Data
public class ErrorsResponse {
    private List<ErrorMessageDto> errors;

    public ErrorsResponse(List<ErrorMessageDto> errors) {
        this.errors = errors;
    }
}
