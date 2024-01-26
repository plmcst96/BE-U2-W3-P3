package cristinapalmisani.BEU2W3P3.controller;

import cristinapalmisani.BEU2W3P3.entities.Event;
import cristinapalmisani.BEU2W3P3.exception.BadRequestException;
import cristinapalmisani.BEU2W3P3.payload.event.EventDTO;
import cristinapalmisani.BEU2W3P3.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public Page<Event> getEvents(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sort) {
        return eventService.findAll(page, size, sort);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event createEvent(@RequestBody @Validated EventDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return eventService.save(body);
        }
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable UUID id) {
        return eventService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event updateEventById(@PathVariable UUID id, @RequestBody EventDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return eventService.updateById(id, body);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public void deleteEventById(@PathVariable UUID id) {
        eventService.deleteById(id);
    }

    @PostMapping("/{id}/picture")
    public String uploadExample(@PathVariable UUID id, @RequestParam("picture") MultipartFile body) throws IOException {
        return eventService.uploadPicture(id, body);
    }
}
