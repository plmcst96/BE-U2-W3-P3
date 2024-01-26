package cristinapalmisani.BEU2W3P3.payload.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ErrorPayloadWithListDTO extends ErrorPayload {
    List<String> errorsList;

    public ErrorPayloadWithListDTO(String message, Date date, List<String> errorsList) {
        super(message, date);
        this.errorsList = errorsList;
    }
}
