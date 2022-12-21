package ru.practicum.mappers;

import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.format.DataTime;
import ru.practicum.models.Event;

import java.time.LocalDateTime;


public class EventMapper {
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate().format(DataTime.formatter))
                .paid(event.isPaid())
                .initiator(UserMapper.toUserDtoShort(event.getInitiator()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(0)
                .views(0)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate().format(DataTime.formatter))
                .description(event.getDescription())
                .paid(event.isPaid())
                .initiator(UserMapper.toUserDtoShort(event.getInitiator()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(DataTime.formatter))
                .views(0)
                .requestModeration(event.isRequestModeration())
                .publishedOn((event.getState() == Event.State.PUBLISHED) ? event.getPublishedOn().format(DataTime.formatter) : null)
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .build();
    }

    public static Event toEvent(NewEventDto eventDto) {
        boolean paid = false;
        Integer participantLimit = 0;
        boolean requestModeration = false;

        if (eventDto.getPaid() != null) {
            paid = eventDto.getPaid();
        }

        if (eventDto.getRequestModeration() != null) {
            requestModeration = eventDto.getRequestModeration();
        }

        if (eventDto.getParticipantLimit() != null) {
            participantLimit = eventDto.getParticipantLimit();
        }

        return Event.builder()
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(DataTime.getDataTime(eventDto.getEventDate()))
                .createdOn(LocalDateTime.now())
                .location(eventDto.getLocation())
                .paid(paid)
                .participantLimit(participantLimit)
                .requestModeration(requestModeration)
                .state(Event.State.PENDING)
                .build();
    }

}

