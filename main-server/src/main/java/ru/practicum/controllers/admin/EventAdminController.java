package ru.practicum.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.events.AdminUpdateEventRequest;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.SearchEventAdmin;
import ru.practicum.format.DataTime;
import ru.practicum.service.event.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/events")
@AllArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEvents(@RequestParam(required = false) Long[] users,
                                         @RequestParam(required = false) String[] states,
                                         @RequestParam(required = false) Long[] categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                         @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        SearchEventAdmin searchEventAdmin = SearchEventAdmin.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeEnd(rangeEnd != null ? LocalDateTime.parse(rangeEnd, DataTime.formatter) : null)
                .rangeStart(rangeStart != null ? LocalDateTime.parse(rangeStart, DataTime.formatter) : null)
                .build();
        log.info("Get events with parametrs");
        return eventService.findAllEventsAdmin(searchEventAdmin, getPageRequest(from, size));
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @RequestBody AdminUpdateEventRequest request) {
        log.trace("Update event id {}", eventId);
        return eventService.updateEventAdmin(eventId, request);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.trace("Event id {} published", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.trace("Event id {} rejected", eventId);
        return eventService.rejectEventAdmin(eventId);
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}
