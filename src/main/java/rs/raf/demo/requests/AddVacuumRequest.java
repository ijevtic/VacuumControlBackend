package rs.raf.demo.requests;

import lombok.Data;

@Data
public class AddVacuumRequest {
    private String name;
    private Boolean active;

}
