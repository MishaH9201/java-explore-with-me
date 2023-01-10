package ru.practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import ru.practicum.models.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {
    @Size(max = 2000)
    @NotBlank
    private String annotation;
    @NotNull
    @Positive
    private Long category;
    @Size(max = 2000)
    @NotBlank
    private String description;
    @Future
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    @NotBlank
    @Size(max = 120)
    private String title;
}
