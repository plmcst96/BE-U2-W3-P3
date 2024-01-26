package cristinapalmisani.BEU2W3P3.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import cristinapalmisani.BEU2W3P3.entities.Event;
import cristinapalmisani.BEU2W3P3.entities.User;
import cristinapalmisani.BEU2W3P3.exception.NotFoundException;
import cristinapalmisani.BEU2W3P3.exception.ParticipatingException;
import cristinapalmisani.BEU2W3P3.payload.event.EventDTO;
import cristinapalmisani.BEU2W3P3.repositories.EventDAO;
import cristinapalmisani.BEU2W3P3.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private Cloudinary cloudinary;

    public Event save(EventDTO body) {
        Event event = new Event();
        event.setTitle(body.title());
        event.setDescription(body.description());
        event.setDate(LocalDate.parse(body.date()));
        event.setLocation(body.location());
        event.setMaxPartecipants(body.maxParticipants());
        return eventDAO.save(event);
    }

    public Event findById(UUID id) {
        return eventDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Event bookEvent(UUID eventId, UUID userId) {
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

    public Page<Event> findAll(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventDAO.findAll(pageable);
    }

    public String uploadPicture(UUID id, MultipartFile body) throws IOException {
        Event event = eventDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(body.getBytes(), ObjectUtils.emptyMap()).get("public_id");
        event.setPictureEvent(url);
        eventDAO.save(event);
        return url;
    }

    public Event updateById(UUID id, EventDTO body) {
        Event event = eventDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        event.setTitle(body.title());
        event.setDescription(body.description());
        event.setDate(LocalDate.parse(body.date()));
        event.setLocation(body.location());
        event.setMaxPartecipants(body.maxParticipants());
        return eventDAO.save(event);
    }

    public void deleteById(UUID id) {
        Event event = eventDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        eventDAO.delete(event);
    }

}
