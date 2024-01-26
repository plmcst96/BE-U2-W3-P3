package cristinapalmisani.BEU2W3P3.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private UUID uuid;
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    @Column(name = "picture_event")
    private String pictureEvent;
    @Column(name = "max_partecipants")
    private int maxPartecipants;
    @ManyToMany
    @JoinTable(name = "events_users", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    @JsonIgnore
    private List<User> users;
}
