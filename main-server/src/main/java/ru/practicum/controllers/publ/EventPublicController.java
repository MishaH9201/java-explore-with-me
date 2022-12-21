package ru.practicum.controllers.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.SearchEvent;
import ru.practicum.format.DataTime;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/events")
@Validated
@Slf4j
public class EventPublicController {
    private final EventService eventService;
    private final StatsClient client;

    @GetMapping
    public List<EventShortDto> findEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "10") int size,
            HttpServletRequest request) {
        SearchEvent searchEvent = SearchEvent.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart != null ? DataTime.getDataTime(rangeStart) : null)
                .rangeEnd(rangeEnd != null ? DataTime.getDataTime(rangeEnd) : null)
                .onlyAvailable(onlyAvailable)
                .build();
        PageRequest pageRequest = getPageRequest(from, size);
        client.addStats(request);
        log.info("Get events by parametrs");
        return eventService.findAllEventsPublic(searchEvent, pageRequest, sort);
    }

    @GetMapping("/{id}")
    public EventFullDto findEvent(@PathVariable Long id, HttpServletRequest request) {
        client.addStats(request);
        log.info("Get events by id {}", id);
        return eventService.getEventById(id);
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("eventDate"));
    }
}
