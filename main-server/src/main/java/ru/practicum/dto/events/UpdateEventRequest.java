package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UpdateEventRequest {
    @NotNull
    private Long eventId;
    private String title;
    private String annotation;
    private String description;
    private String eventDate;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    @Positive
    private Long category;
}
