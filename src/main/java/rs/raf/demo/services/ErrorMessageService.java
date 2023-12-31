package rs.raf.demo.services;

import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Vacuum;

import java.util.List;

public interface ErrorMessageService {
    public boolean add(String message, Vacuum vacuum);
    public List<ErrorMessage> get(Vacuum vacuum);
}
