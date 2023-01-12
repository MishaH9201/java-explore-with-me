package ru.practicum.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.util.DataTime;
import ru.practicum.models.Event;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
                .eventDate(event.getEventDate())
                .description(event.getDescription())
                .paid(event.isPaid())
                .initiator(UserMapper.toUserDtoShort(event.getInitiator()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(0)
                .createdOn(event.getCreatedOn())
                .views(0)
                .requestModeration(event.isRequestModeration())
                .publishedOn((event.getState() == Event.State.PUBLISHED) ? event.getPublishedOn() : null)
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .build();
    }

    public static Event toEvent(NewEventDto eventDto) {
        return Event.builder()
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate())
                .createdOn(LocalDateTime.now())
                .location(eventDto.getLocation())
                .paid(eventDto.isPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.isRequestModeration())
                .state(Event.State.PENDING)
                .build();
    }
}

