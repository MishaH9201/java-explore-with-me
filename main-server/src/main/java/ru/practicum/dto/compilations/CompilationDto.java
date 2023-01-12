package ru.practicum.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.dto.events.EventShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private boolean pinned;
    private String title;
}
