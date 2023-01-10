package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto create(@Valid @RequestBody NewCompilationDto createCompilationDto) {
        log.info("Compilation {} create", createCompilationDto);
        return compilationService.create(createCompilationDto);
    }

    @PatchMapping(value = "/{compId}/events/{eventId}")
    public void createEvent(@PathVariable @Positive long compId,
                            @PathVariable @Positive long eventId) {
        log.info("Event {} in Compilation {} create", eventId, compId);
        compilationService.createEvent(compId, eventId);
    }

    @DeleteMapping(value = "/{compId}")
    public void deleteById(@PathVariable @Positive long compId) {
        compilationService.deleteById(compId);
        log.info("Compilation id {} delete", compId);
    }

    @DeleteMapping(value = "/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable @Positive long compId,
                            @PathVariable @Positive long eventId) {
        compilationService.deleteEvent(compId, eventId);
        log.info("Event id {} in Compilation id {} delete", eventId, compId);
    }

    @PatchMapping(value = "/{compId}/pin")
    public void pin(@PathVariable @Positive long compId) {
        compilationService.pin(compId, true);
        log.info("Collection id {} is pinned", compId);
    }

    @DeleteMapping(value = "/{compId}/pin")
    public void unpin(@PathVariable @Positive long compId) {
        compilationService.unpin(compId, false);
        log.info("Collection id {} is unpinned", compId);
    }
}
