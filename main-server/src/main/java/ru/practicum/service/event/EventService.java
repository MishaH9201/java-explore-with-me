package ru.practicum.service.event;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.events.AdminUpdateEventRequest;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.events.*;
import ru.practicum.format.DataTime;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.models.*;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.RequestRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.service.StatsService;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsService statsService;


    public List<EventShortDto> findAllEvents(Long userId, PageRequest pageRequest) {
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return repository.findAllByInitiatorId(userId, pageRequest).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto publishEvent(long eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        LocalDateTime publishTime = LocalDateTime.now();

        if (event.getEventDate().minusHours(1).isBefore(publishTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect time frames");
        }
        if (event.getState() != Event.State.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PENDING events can be published");
        }
        event.setPublishedOn(publishTime);
        event.setState(Event.State.PUBLISHED);
        event.setConfirmedRequests(requestRepository.getConfirmedRequestsAmount(eventId));
        EventFullDto eventFullDto = EventMapper.toEventFullDto(repository.save(event));
        eventFullDto.setViews(statsService.getCount(eventId));
        return eventFullDto;
    }

    public EventFullDto rejectEventAdmin(long eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getState() == Event.State.PUBLISHED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't be changed PENDING events");
        }
        event.setState(Event.State.CANCELED);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(repository.save(event));
        eventFullDto.setViews(statsService.getCount(eventId));
        return eventFullDto;
    }

    public EventFullDto updateEvent(long userId, UpdateEventRequest request) {
        Event basicEvent = repository.findById(request.getEventId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if ((basicEvent.getState() != Event.State.PENDING)
                && (basicEvent.getState() != Event.State.CANCELED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PENDING and CANCELED events can be updated");
        }
        if (basicEvent.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time error");
        }
        if (request.getAnnotation() != null && !request.getAnnotation().isBlank()) {
            basicEvent.setAnnotation(request.getAnnotation());
        }
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            basicEvent.setTitle(request.getTitle());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            basicEvent.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            basicEvent.setEventDate(LocalDateTime.parse(request.getEventDate(), DataTime.formatter));
        }
        if (request.getPaid() != null) {
            basicEvent.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            basicEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (basicEvent.getState() == Event.State.CANCELED) {
            basicEvent.setState(Event.State.PENDING);
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(repository.save(basicEvent));
        eventFullDto.setViews(statsService.getCount(request.getEventId()));
        return eventFullDto;
    }


    public EventFullDto updateEventAdmin(long eventId, AdminUpdateEventRequest request) {
        Event event = repository.findById(eventId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found.");
        });
        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory()).orElseThrow(() -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
            });
            event.setCategory(category);
        }
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            event.setTitle(request.getTitle());
        }
        if (request.getAnnotation() != null && !request.getAnnotation().isBlank()) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), DataTime.formatter));
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(repository.save(event));
        eventFullDto.setViews(statsService.getCount(eventId));
        return eventFullDto;
    }

    public EventFullDto canceledEvent(Long userId, Long eventId) {
        Event basicEvent = repository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (!userId.equals(basicEvent.getInitiator().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only cancel your own events");
        }
        if (basicEvent.getState() != Event.State.PENDING) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cancel only PENDING events");
        }
        basicEvent.setState(Event.State.CANCELED);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(repository.save(basicEvent));
        eventFullDto.setViews(statsService.getCount(eventId));
        return eventFullDto;
    }

    public EventFullDto addEvent(long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        Event event = EventMapper.toEvent(eventDto);
        event.setInitiator(user);
        event.setCategory(category);
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Time error");
        }
        return EventMapper.toEventFullDto(repository.save(event));
    }

    public EventFullDto getEvent(long userId, long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User  not found"));
        Event event = repository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (event.getInitiator().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User ID " + userId + "not corresponding to requested event initiator.");
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(repository.save(event));
        eventFullDto.setViews(statsService.getCount(eventId));
        return eventFullDto;
    }

    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID " + userId + "not corresponding to requested event initiator.");
        }
        return requestRepository.findAllByEvent_Id(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto confirmationOfRequest(Long userId, Long eventId, Long reqId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (event.getParticipantLimit() > 0) {
            int count = requestRepository.getConfirmedRequestsAmount(event.getId());
            if (event.getParticipantLimit() <= requestRepository.getConfirmedRequestsAmount(event.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Places are over");
            }
            request.setState(Request.State.CONFIRMED);
            requestRepository.save(request);
            if (event.getParticipantLimit() > 0) {
                long limit = event.getParticipantLimit();
                if (count >= limit) {
                    request.setState(Request.State.REJECTED);
                    requestRepository.save(request);
                }
            }
        }
        return RequestMapper.toParticipationRequestDto(request);
    }

    public List<EventShortDto> findAllEventsPublic(SearchEvent searchEvent, PageRequest pageRequest, String sort) {
        Iterable<Event> events = repository.findAll(FormatForSearch.formatExpression(searchEvent), pageRequest);
        List<Event> eventList = Lists.newArrayList(events);
        if (!eventList.isEmpty()) {
            Map<Long, Long> statsCount = statsService.getCount(events);
            List<EventShortDto> eventsDto = new ArrayList<>();
            events.forEach((e) -> eventsDto.add(EventMapper.toEventShortDto(e)));
            eventsDto.forEach((e) -> {
                e.setConfirmedRequests(requestRepository.getConfirmedRequestsAmount(e.getId()));
                if (statsCount.containsKey(e.getId())) {
                    e.setViews(statsCount.get(e.getId()));
                }
            });
            if (sort.equals("VIEWS")) {
                return eventsDto.stream()
                        .sorted((o1, o2) -> (int) (o1.getViews() - o2.getViews()))
                        .collect(Collectors.toList());
            } else {
                return eventsDto;
            }
        } else {
            return Collections.emptyList();
        }
    }


    public List<EventFullDto> findAllEventsAdmin(SearchEventAdmin searchEventAdmin, PageRequest pageRequest) {
        Iterable<Event> events = repository.findAll(FormatForSearch.formatExpression(searchEventAdmin), pageRequest);
        List<EventFullDto> eventsDto = new ArrayList<>();
        events.forEach((e) -> eventsDto.add(EventMapper.toEventFullDto(e)));
        return eventsDto;
    }

    public EventFullDto getEventById(Long id) {
        Event event = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (event.getState() != Event.State.PUBLISHED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Event is not published");
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(requestRepository.getConfirmedRequestsAmount(id));
        eventFullDto.setViews(statsService.getCount(id));
        return eventFullDto;
    }

    public ParticipationRequestDto rejectEvent(Long userId, Long eventId, Long reqId) {
        repository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        if (request.getState().equals(Request.State.REJECTED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: request ID " + reqId + " REJECTED already.");
        }
        request.setState(Request.State.REJECTED);
        return RequestMapper.toParticipationRequestDto(request);
    }
}
