package cristinapalmisani.BEU2W3P3.exception;

import cristinapalmisani.BEU2W3P3.entities.Event;
import cristinapalmisani.BEU2W3P3.entities.User;
import lombok.Getter;

@Getter
public class ParticipatingException extends RuntimeException{
    public ParticipatingException(String message) {
        super(message);
    }

    public ParticipatingException(Event event, User user) {
        super("User " + user.getName() + " is already participating in event " + event.getTitle());
    }
}
