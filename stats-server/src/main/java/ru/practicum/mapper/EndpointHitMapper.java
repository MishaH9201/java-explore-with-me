package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toEndpointHit(EndpointHitDto dto) {
        return new EndpointHit(dto.getId(),
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                dto.getTimeStamp() != null ? LocalDateTime.parse(dto.getTimeStamp(), formatter) : null);
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit hit) {
        return new EndpointHitDto(hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimeStamp() != null ? hit.getTimeStamp().format(formatter) : null);
    }
}

