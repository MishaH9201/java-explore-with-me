package ru.practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UpdateEventRequest {
    @NotNull
    private Long eventId;
    @Size(max = 120)
    private String title;
    @Size(max = 2000)
    private String annotation;
    @Size(max = 7000)
    private String description;
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    @Positive
    private Long category;
}
