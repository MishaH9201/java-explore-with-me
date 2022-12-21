package ru.practicum.controllers.privet;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.RequestService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Validated
@AllArgsConstructor
public class RequestPrivateController {
    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        log.trace("Get request by user id{}.", userId);
        return service.getRequests(userId);
    }


    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable long userId,
                                              @RequestParam long eventId) {
        log.trace("Create request from user id{}, for event {}", eventId, eventId);
        return service.create(userId, eventId);
    }


    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                 @PathVariable long requestId) {
        log.trace("Request id{}, from user id {} canceled", requestId, userId);
        return service.cancelRequest(userId, requestId);
    }

}
