package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.models.Location;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventRequest {
    Long category;
    String title;
    String annotation;
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Boolean requestModeration;
    Integer participantLimit;
}
