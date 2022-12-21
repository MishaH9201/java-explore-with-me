package ru.practicum.dto.events;

import lombok.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.models.Event;
import ru.practicum.models.Location;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
   private long id;
    private CategoryDto category;
    private String title;
    private String annotation;
    private  String description;
    private  UserDtoShort initiator;
    private  int confirmedRequests;
    private String eventDate;
    private String createdOn;
    private  String publishedOn;
    private Location location;
    private boolean paid;
    private boolean requestModeration;
    private int participantLimit;
    private Event.State state;
    private long views;
}
