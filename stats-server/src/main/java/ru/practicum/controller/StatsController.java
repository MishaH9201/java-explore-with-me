package ru.practicum.controller;

import org.jetbrains.annotations.NotNull;
import ru.practicum.Service.StatsService;
import ru.practicum.dto.EndpointHitDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.ViewStats;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
@RestController
@Slf4j
public class StatsController {
    private final StatsService statsService;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public EndpointHitDto hit(@RequestBody @NotNull EndpointHitDto hit) {
        log.trace("Receiving a POST request for application {} on request to {} from user IP {}.",
                hit.getApp(), hit.getUri(), hit.getIp());
        return EndpointHitMapper.toEndpointHitDto(statsService.add(hit));
    }


    @GetMapping("/stats")
    public List<ViewStats> get(@RequestParam(required = false) String start,
                               @RequestParam(required = false) String end,
                               @RequestParam(required = false) String[] uris,
                               @RequestParam(required = false, defaultValue = "false") boolean unique,
                               @RequestParam(required = false, defaultValue = "ewm-main-service") String app) {
        log.trace("Receiving a POST request: start-{} , end-{}, uris-{}, unique-{}.",
                start, end, Arrays.toString(uris), unique);
        return statsService.get(start != null ? LocalDateTime.parse(start, formatter) : null,
                end != null ? LocalDateTime.parse(end, formatter) : null,
                uris, unique, app);
    }
}
