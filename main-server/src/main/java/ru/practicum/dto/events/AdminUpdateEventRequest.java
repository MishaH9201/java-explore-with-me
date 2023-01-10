package ru.practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.models.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventRequest {
    private Long category;
    @Size(max = 120)
    private String title;
    @Size(max = 2000)
    private String annotation;
    @Size(max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
}
