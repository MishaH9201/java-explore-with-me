package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchEventAdmin {
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Long[] users;
    private Long[] categories;
    private String[] states;
}
