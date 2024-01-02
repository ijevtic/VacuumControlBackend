package rs.raf.demo.services;

import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.responses.ErrorsResponse;

import java.util.List;

public interface ErrorMessageService {
    public boolean add(String message, Vacuum vacuum);
    public ErrorsResponse get();
}
