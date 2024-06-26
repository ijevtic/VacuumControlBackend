package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.requests.AddUserRequest;
import rs.raf.demo.requests.AddVacuumRequest;
import rs.raf.demo.requests.ScheduleRequest;
import rs.raf.demo.requests.SearchRequest;
import rs.raf.demo.responses.VacuumsResponse;
import rs.raf.demo.security.CheckSecurity;
import rs.raf.demo.services.ErrorMessageService;
import rs.raf.demo.services.UserService;
import rs.raf.demo.services.VacuumService;

import javax.validation.Valid;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static rs.raf.demo.utils.Constants.*;

@RestController
@RequestMapping("/vacuum")
@CrossOrigin
public class VacuumController {
    private VacuumService vacuumService;
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    public VacuumController(VacuumService vacuumService, ThreadPoolTaskScheduler taskScheduler) {
        this.vacuumService = vacuumService;
        this.taskScheduler = taskScheduler;
        this.taskScheduler.setPoolSize(10);
    }

    @CheckSecurity(role = SEARCHV)
    @PostMapping(value= "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumsResponse> search(@RequestBody SearchRequest request) {
        VacuumsResponse vacuums = this.vacuumService.search(request);
        if(vacuums == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(vacuums, HttpStatus.OK);
    }

    @CheckSecurity(role = ADDV)
    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumDto> add(@RequestBody AddVacuumRequest request) {
        if(!this.vacuumService.add(request))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CheckSecurity(role = REMOVEV)
    @DeleteMapping(value = "/remove/{vacuumName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumDto> remove(@PathVariable String vacuumName) {
        if (!this.vacuumService.remove(vacuumName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CheckSecurity(role = DISCHARGEV)
    @PostMapping(value = "/discharge/{vacuumName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumDto> discharge(@PathVariable String vacuumName) {
        if (!this.vacuumService.dischargeVacuum(vacuumName, false, false)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @CheckSecurity(role = STARTV)
    @PostMapping(value = "/start/{vacuumName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumDto> startVacuum(@PathVariable String vacuumName) {
        if (!this.vacuumService.startVacuum(vacuumName, false)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @CheckSecurity(role = STOPV)
    @PostMapping(value = "/stop/{vacuumName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacuumDto> stopVacuum(@PathVariable String vacuumName) {
        if (!this.vacuumService.stopVacuum(vacuumName, false)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @CheckSecurity(role = DISCHARGEV)
    @PostMapping(value = "/schedule-discharge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> scheduleDischarge(@Valid @RequestBody ScheduleRequest request) {
        CronTrigger cronTrigger = getCronTrigger(request);

        this.taskScheduler.schedule(() -> {
            this.vacuumService.dischargeVacuum(request.getVacuumName(), true, false);
        }, cronTrigger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CheckSecurity(role = STARTV)
    @PostMapping(value = "/schedule-start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> scheduleStart(@Valid @RequestBody ScheduleRequest request) {
        CronTrigger cronTrigger = getCronTrigger(request);

        this.taskScheduler.schedule(() -> {
            this.vacuumService.startVacuum(request.getVacuumName(), true);
        }, cronTrigger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CheckSecurity(role = STOPV)
    @PostMapping(value = "/schedule-stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> scheduleStop(@Valid @RequestBody ScheduleRequest request) {
        CronTrigger cronTrigger = getCronTrigger(request);

        this.taskScheduler.schedule(() -> {
            this.vacuumService.stopVacuum(request.getVacuumName(), true);
        }, cronTrigger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private CronTrigger getCronTrigger(ScheduleRequest request) {
        Instant instant = Instant.ofEpochSecond(request.getTime());

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        String cronExpression = String.format("%d %d %d %d %d ?",
                localDateTime.getSecond(), localDateTime.getMinute(), localDateTime.getHour(),
                localDateTime.getDayOfMonth(), localDateTime.getMonthValue());

        return new CronTrigger(cronExpression);
    }
}
