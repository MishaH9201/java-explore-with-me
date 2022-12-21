package ru.practicum.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "location_lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))})
    private Location location;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @ManyToMany(mappedBy = "events")
    Set<Compilation> compilations;

    public enum State {
        PENDING, PUBLISHED, CANCELED
    }
}
