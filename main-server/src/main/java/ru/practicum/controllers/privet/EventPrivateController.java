package ru.practicum.controllers.privet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.dto.events.UpdateEventRequest;
import ru.practicum.service.event.EventService;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
@Slf4j
public class EventPrivateController {
    private final EventService eventService;


    @PatchMapping
    public EventFullDto update(@PathVariable long userId,
                               @Valid @RequestBody UpdateEventRequest request) {
        log.trace("Update request user id{}.", userId);
        return eventService.updateEvent(userId, request);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto canceled(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.trace("Event canceled {}.", eventId);
        return eventService.canceled(userId, eventId);
    }

    @PostMapping
    public EventFullDto add(@PathVariable long userId,
                            @RequestBody @Valid NewEventDto eventDto) {
        log.trace("Create Event user id{}.", userId);
        return eventService.add(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto get(@PathVariable long userId,
                            @PathVariable long eventId) {
        log.trace("Get event by id {}.", eventId);
        return eventService.getById(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.trace("Get request by event id {}.", eventId);
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmationOfRequest(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @PathVariable Long reqId) {
        log.trace("Request for participation in the event {} from user{} was confirmed", eventId, userId);
        return eventService.confirmationOfRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectionOfRequest(@PathVariable Long userId,
                                                      @PathVariable Long eventId,
                                                      @PathVariable Long reqId) {
        log.trace("Request for participation in the event {} from user{} was rejected", eventId, userId);
        return eventService.rejectEvent(userId, eventId, reqId);
    }

    @GetMapping
    public List<EventShortDto> findAllByUserId(@PathVariable @Positive Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get events by user id {},from {}, size {}", userId, from, size);
        return eventService.findAllByUserId(userId, getPageRequest(from, size));
    }

    private static PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}
