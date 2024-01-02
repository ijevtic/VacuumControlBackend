package rs.raf.demo.dto;

import lombok.Data;
import rs.raf.demo.model.Vacuum;

@Data
public class ErrorMessageDto {
    private String message;
    private Long dateCreated;
    private String vacuumName;
}
