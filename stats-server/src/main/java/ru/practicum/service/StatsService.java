package ru.practicum.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;

import ru.practicum.model.QEndpointHit;
import ru.practicum.model.ViewStats;
import org.springframework.stereotype.Service;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {
    private final StatsRepository statsRepository;

    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique, String app) {
        BooleanExpression filter;
        filter = QEndpointHit.endpointHit.uri.in(uris)
                .and(QEndpointHit.endpointHit.timeStamp.after(start))
                .and(QEndpointHit.endpointHit.timeStamp.before(end))
                .and(QEndpointHit.endpointHit.app.in(app));
        List<ViewStats> result = new ArrayList<>();
        try {
            Iterable<EndpointHit> hits = statsRepository.findAll(filter);
            List<EndpointHit> resultHits = Lists.newArrayList(hits);
            for (String uri : uris) {
                long views = (unique) ?
                        resultHits.stream().filter(x -> x.getUri().equals(uri)).distinct().count() :
                        resultHits.stream().filter(x -> x.getUri().equals(uri)).count();
                result.add(new ViewStats(app, uri, (int) views));
            }
            return result;
        } catch (Exception e) {
            return result;
        }
    }

@Transactional
    public EndpointHit add(EndpointHitDto hitDto) {
        EndpointHit hit = EndpointHitMapper.toEndpointHit(hitDto);
        return statsRepository.save(hit);
    }
}


