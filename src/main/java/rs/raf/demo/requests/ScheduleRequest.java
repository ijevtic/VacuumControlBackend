package rs.raf.demo.requests;

import lombok.Data;

@Data
public class ScheduleRequest {
    private String vacuumName;
    private Long time;
}
