package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.repositories.CompilationRepository;
import ru.practicum.repositories.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, PageRequest pageRequest) {
        if (pinned == null) {
            return repository.findAll(pageRequest).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findByPinned(pinned, pageRequest).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    public CompilationDto getCompilation(long compId) {
        Compilation result = repository.findById(compId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found"));
        return CompilationMapper.toCompilationDto(result);
    }

    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        compilation.setEvents(new HashSet<>(events) {
        });
        return CompilationMapper.toCompilationDto(repository.save(compilation));

    }

    public void createEvent(long compId, long eventId) {
        Compilation result = repository.findById(compId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        Set<Event> events = result.getEvents();
        events.add(event);
        result.setEvents(events);
        repository.save(result);

    }

    public void deleteById(long compId) {
        repository.findById(compId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found"));
        repository.deleteById(compId);
    }

    public void deleteEvent(long compId, long eventId) {
        Compilation result = repository.findById(compId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (result.getEvents().contains(event)) {
            result.getEvents().remove(event);
            repository.save(result);
        }
    }

    public void pin(long compId, boolean pinned) {
        Compilation result = repository.findById(compId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compilation not found"));
        result.setPinned(pinned);
        repository.save(result);
    }
}
