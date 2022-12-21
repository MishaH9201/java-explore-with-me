package ru.practicum.mappers;

import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.format.DataTime;
import ru.practicum.models.Request;


public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreated().format(DataTime.formatter))
                .status(request.getState().name())
                .build();
    }
}
