package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.dto.VacuumDto;
import rs.raf.demo.requests.AddUserRequest;
import rs.raf.demo.requests.SearchRequest;
import rs.raf.demo.security.CheckSecurity;
import rs.raf.demo.services.UserService;
import rs.raf.demo.services.VacuumService;

import javax.validation.Valid;

import java.util.List;

import static rs.raf.demo.utils.Constants.*;

@RestController
@RequestMapping("/vacuum")
@CrossOrigin
public class VacuumController {
    private VacuumService vacuumService;

    @Autowired
    public VacuumController(VacuumService vacuumService) {
        this.vacuumService = vacuumService;
    }

    @CheckSecurity(role = SEARCHV)
    @GetMapping(value= "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VacuumDto>> search(@RequestBody SearchRequest request) {
        List<VacuumDto> vacuums = this.vacuumService.search(request);
        if(vacuums == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(vacuums, HttpStatus.CREATED);
    }
}
