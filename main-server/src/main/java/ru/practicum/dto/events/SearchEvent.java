package ru.practicum.dto.events;

import lombok.*;
import ru.practicum.models.Event;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchEvent {
    private String text;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean paid;
    private Boolean onlyAvailable;
    private Long[] users;
    private Long[] categories;
    private Event.State[] states;

}
