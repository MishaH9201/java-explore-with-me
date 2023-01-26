package ru.practicum.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private StateComment stateComment;

    @Column(name = "participant")
    private boolean participant;

    @Column(name = "moderation")
    private boolean moderation;


    public enum StateComment {
        PENDING, NOT_REQUIRING
    }

}
