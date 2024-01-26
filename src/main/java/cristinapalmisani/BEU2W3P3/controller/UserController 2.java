package cristinapalmisani.BEU2W3P3.controller;

import cristinapalmisani.BEU2W3P3.entities.Event;
import cristinapalmisani.BEU2W3P3.entities.User;
import cristinapalmisani.BEU2W3P3.exception.BadRequestException;
import cristinapalmisani.BEU2W3P3.exception.NotFoundException;
import cristinapalmisani.BEU2W3P3.payload.user.UserDTO;
import cristinapalmisani.BEU2W3P3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sort) {
        return userService.getUsers(page, size, sort);
    }

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/me/events")
    public Page<Event> getEvents(@AuthenticationPrincipal User user,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sort) {
        return userService.getEvents(user, page, size, sort);
    }

    @GetMapping("/me/events/{id}/book")
    public Event bookEvent(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        return userService.bookEvent(user, id);
    }

    @GetMapping("/me/events/{id}/unbook")
    public Event unBookEvent(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        return userService.unBookEvent(user, id);
    }

    @GetMapping("/{userId}/events/{eventId}/book")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Event bookEvent(@PathVariable UUID userId, @PathVariable UUID eventId) {
        return userService.bookEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/unbook")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Event unBookEvent(@PathVariable UUID userId, @PathVariable UUID eventId) {
        return userService.unBookEvent(userId, eventId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable UUID id) {
        try {
            return userService.findById(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(id, body);
    }

    @GetMapping("/{id}/set-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User setAdmin(@PathVariable UUID id) {
        return userService.setAdmin(id);
    }

}
