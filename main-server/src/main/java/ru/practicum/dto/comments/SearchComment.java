package ru.practicum.dto.comments;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchComment {
    Long[] users;
    String state;
    Long[] events;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
}
