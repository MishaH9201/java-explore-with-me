package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointHitMapper {


    public static EndpointHit toEndpointHit(EndpointHitDto dto) {
        return new EndpointHit(dto.getId(),
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                dto.getTimeStamp());
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit hit) {
        return new EndpointHitDto(hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimeStamp());
    }
}

