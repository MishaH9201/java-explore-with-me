package ru.practicum.models.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EndpointHit {

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timeStamp;
}
