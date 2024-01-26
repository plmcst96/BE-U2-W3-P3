package cristinapalmisani.BEU2W3P3.service;

import com.cloudinary.provisioning.Account;
import cristinapalmisani.BEU2W3P3.entities.Event;
import cristinapalmisani.BEU2W3P3.entities.Role;
import cristinapalmisani.BEU2W3P3.entities.User;
import cristinapalmisani.BEU2W3P3.exception.NotFoundException;
import cristinapalmisani.BEU2W3P3.exception.ParticipatingException;
import cristinapalmisani.BEU2W3P3.payload.user.UserDTO;
import cristinapalmisani.BEU2W3P3.repositories.EventDAO;
import cristinapalmisani.BEU2W3P3.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EventDAO eventDAO;

    public Page<User> getUsers(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userDAO.findAll(pageable);
    }

    public User findById(UUID id) throws NotFoundException {
        return userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }


    public User findByIdAndUpdate(UUID id, UserDTO body) {
        User user = userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        user.setEmail(body.email());
        user.setName(body.name());
        user.setSurname(body.surname());
        return userDAO.save(user);
    }

    public User setAdmin(UUID id) {
        User user = userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        user.setRole(Role.EVENT_MANAGER);
        return userDAO.save(user);
    }

    public User setUser(UUID id) {
        User user = userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        user.setRole(Role.USER);
        return userDAO.save(user);
    }

    public Event bookEvent(User authUser, UUID eventId) {
        Event event = eventDAO.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userDAO.findById(authUser.getUuid()).orElseThrow(() -> new NotFoundException(authUser.getUuid()));
        if (event.getUsers().contains(user)) {
            throw new ParticipatingException(event, user);
        } else {
            if (event.getUsers().size() < event.getMaxPartecipants()) {
                event.getUsers().add(user);
                return eventDAO.save(event);
            } else {
                throw new ParticipatingException("SOLD OUT!");
            }
        }
    }

    public Event bookEvent(UUID userId, UUID eventId) {
        Event event = eventDAO.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        if (event.getUsers().contains(user)) {
            throw new ParticipatingException(event, user);
        } else {
            if (event.getUsers().size() < event.getMaxPartecipants()) {
                event.getUsers().add(user);
                return eventDAO.save(event);
            } else {
                throw new ParticipatingException("SOLD OUT!");
            }
        }
    }

    public Event unBookEvent(User authUser, UUID eventId) {
        Event event = eventDAO.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userDAO.findById(authUser.getUuid()).orElseThrow(() -> new NotFoundException(authUser.getUuid()));
        if (event.getUsers().contains(user)) {
            event.getUsers().remove(user);
            return eventDAO.save(event);
        } else {
            throw new ParticipatingException("NOT PARTICIPATING!");
        }
    }

    public Event unBookEvent(UUID userId, UUID eventId) {
        Event event = eventDAO.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        if (event.getUsers().contains(user)) {
            event.getUsers().remove(user);
            return eventDAO.save(event);
        } else {
            throw new ParticipatingException("NOT PARTICIPATING!");
        }
    }

    public Page<Event> getEvents(User user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventDAO.findByUsers(user, pageable);
    }
}
