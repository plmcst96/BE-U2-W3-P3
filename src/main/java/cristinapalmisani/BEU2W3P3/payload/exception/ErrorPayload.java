package cristinapalmisani.BEU2W3P3.payload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorPayload {
    private String message;
    private Date timestamp;
}
