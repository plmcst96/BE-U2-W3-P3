package cristinapalmisani.BEU2W3P3.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NotFoundException extends RuntimeException{
    public NotFoundException(UUID id) {
        super("id " + id + " not found!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
