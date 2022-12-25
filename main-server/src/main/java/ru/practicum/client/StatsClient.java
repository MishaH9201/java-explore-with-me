package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.format.DataTime;
import ru.practicum.models.stats.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsClient {
    private String url;
    private String app = "ewm-main-service";

    @Autowired
    public StatsClient(@Value("${STATS-SERVER_URL}") String url) {
        this.url = url;
    }

    private WebClient client = WebClient.create(url);

    public void addStats(HttpServletRequest request) {
        EndpointHitDto hit = new EndpointHitDto(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(DataTime.formatter));
        client.post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(hit), EndpointHitDto.class)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server is not responding")))
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server is not responding")))
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<ViewStats> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        Flux<ViewStats> stats = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start.format(DataTime.formatter))
                        .queryParam("end", end.format(DataTime.formatter))
                        .queryParam("unique", unique)
                        .queryParam("uris", uris)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Server is not responding")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server is not responding")))
                .bodyToFlux(ViewStats.class);
        return stats.collectList().block();
    }
}

