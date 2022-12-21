package ru.practicum.dto.events;

import lombok.*;

import ru.practicum.models.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {


    @NotBlank
    private String annotation;

    @NotNull
    @Positive
    private Long category;

    @NotBlank
    private String description;

    @NotNull
    private String eventDate;

    @NotNull
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;
    @NotBlank
    private String title;
}
