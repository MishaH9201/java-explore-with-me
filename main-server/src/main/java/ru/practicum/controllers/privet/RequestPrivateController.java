package ru.practicum.controllers.privet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getAllByUserId(@PathVariable long userId) {
        log.trace("Get request by user id{}.", userId);
        return service.getAllByUserId(userId);
    }


    @PostMapping
    public ParticipationRequestDto add(@PathVariable long userId,
                                       @RequestParam long eventId) {
        log.trace("Create request from user id{}, for event {}", eventId, eventId);
        return service.create(userId, eventId);
    }


    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable long userId,
                                          @PathVariable long requestId) {
        log.trace("Request id{}, from user id {} canceled", requestId, userId);
        return service.cancel(userId, requestId);
    }

}
