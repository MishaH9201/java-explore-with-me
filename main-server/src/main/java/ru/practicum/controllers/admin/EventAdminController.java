package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.events.AdminUpdateEventRequest;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.SearchEventAdmin;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEvents(@RequestParam(required = false) Long[] users,
                                         @RequestParam(required = false) String[] states,
                                         @RequestParam(required = false) Long[] categories,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @Positive @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        SearchEventAdmin searchEventAdmin = SearchEventAdmin.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeEnd(rangeEnd)
                .rangeStart(rangeStart)
                .build();
        log.info("Get events with parametrs");
        return eventService.findAllEventsAdmin(searchEventAdmin, getPageRequest(from, size));
    }

    @PutMapping("/{eventId}")
    public EventFullDto update(@PathVariable long eventId,
                               @RequestBody @Valid AdminUpdateEventRequest request) {
        log.trace("Update event id {}", eventId);
        return eventService.updateEventAdmin(eventId, request);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable long eventId) {
        log.trace("Event id {} published", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(@PathVariable long eventId) {
        log.trace("Event id {} rejected", eventId);
        return eventService.rejectEventAdmin(eventId);
    }

    private static PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}
