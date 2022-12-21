package ru.practicum.service;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.models.Event;
import ru.practicum.models.Request;
import ru.practicum.models.User;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.RequestRepository;
import ru.practicum.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<ParticipationRequestDto> getRequests(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return repository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto create(long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (repository.findByEvent_IdAndRequester_Id(eventId, userId) != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request already exists");
        }
        if (event.getInitiator().getId() == userId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation request to own event");
        }
        if (event.getState() != Event.State.PUBLISHED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event is not published yet");
        }
        int confirmedRequests = repository.getConfirmedRequestsAmount(eventId);
        if (confirmedRequests == event.getParticipantLimit()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Participant limit reached");
        }
        Request request = Request.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .build();
        if (!event.isRequestModeration()) {
            request.setState(Request.State.CONFIRMED);
        } else {
            request.setState(Request.State.PENDING);
        }
        Request newRequest = repository.save(request);
        return RequestMapper.toParticipationRequestDto(newRequest);
    }

    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Request request = repository.findByIdAndRequester_Id(requestId, userId);
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request  not found.");
        }
        request.setState(Request.State.CANCELED);
        return RequestMapper.toParticipationRequestDto(repository.save(request));
    }
}
