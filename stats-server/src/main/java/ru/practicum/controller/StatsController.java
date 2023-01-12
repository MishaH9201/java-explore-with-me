package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.service.StatsService;
import ru.practicum.dto.EndpointHitDto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.ViewStats;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@RestController
@Slf4j
public class StatsController {
    private final StatsService statsService;


    @PostMapping("/hit")
    public EndpointHitDto add(@RequestBody EndpointHitDto hit) {
        log.trace("Receiving a POST request for application {} on request to {} from user IP {}.",
                hit.getApp(), hit.getUri(), hit.getIp());
        return EndpointHitMapper.toEndpointHitDto(statsService.add(hit));
    }


    @GetMapping("/stats")
    public List<ViewStats> get(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                               @RequestParam(required = false) String[] uris,
                               @RequestParam(defaultValue = "false") boolean unique,
                               @RequestParam(defaultValue = "ewm-main-service") String app) {
        log.trace("Receiving a POST request: start-{} , end-{}, uris-{}, unique-{}.",
                start, end, Arrays.toString(uris), unique);
        return statsService.get(start, end, uris, unique, app);
    }
}
