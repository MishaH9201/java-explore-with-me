package ru.practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.models.Event;
import ru.practicum.models.Location;

import java.time.LocalDateTime;


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
    private String description;
    private UserDtoShort initiator;
    private long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Location location;
    private boolean paid;
    private boolean requestModeration;
    private int participantLimit;
    private Event.State state;
    private long views;
}
