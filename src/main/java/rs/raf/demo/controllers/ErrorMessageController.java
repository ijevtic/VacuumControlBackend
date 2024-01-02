package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.demo.responses.ErrorsResponse;
import rs.raf.demo.responses.UsersResponse;
import rs.raf.demo.security.CheckSecurity;
import rs.raf.demo.services.ErrorMessageService;

import static rs.raf.demo.utils.Constants.READ;

@RestController
@CrossOrigin
@RequestMapping("/error-history")
public class ErrorMessageController {
    private ErrorMessageService errorMessageService;

    @Autowired
    public ErrorMessageController(ErrorMessageService errorMessageService) {
        this.errorMessageService = errorMessageService;
    }

    @GetMapping(value= "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ErrorsResponse> getErrorHistory() {
        return new ResponseEntity<>(this.errorMessageService.get(), HttpStatus.OK);
    }
}
