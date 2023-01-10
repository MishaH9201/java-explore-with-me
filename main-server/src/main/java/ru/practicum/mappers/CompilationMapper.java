package ru.practicum.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.models.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getEvents().stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return new CompilationDto(compilation.getId(), events, compilation.isPinned(), compilation.getTitle());
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .build();
    }
}

