package cristinapalmisani.BEU2W3P3.exception;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException{
    private List<ObjectError> errorList;

    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(List<ObjectError> errors){
        super("Bad request");
        this.errorList = errors;
    }
}
