package ru.practicum.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.util.DataTime;
import ru.practicum.models.request.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
