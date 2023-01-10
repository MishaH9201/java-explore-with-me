package ru.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.models.Event;
import ru.practicum.models.stats.ViewStats;
import ru.practicum.repositories.EventRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsClient client;
    private final EventRepository eventRepository;

    public Map<Long, Long> getCount(Iterable<Event> events) {
        Set<Long> eventIds = new HashSet<>();
        events.forEach(e -> eventIds.add(e.getId()));
        return getCountView(eventIds);
    }

    public Long getCount(Long id) {
        Set<Long> eventIds = new HashSet<>();
        eventIds.add(id);
        Map<Long, Long> result = getCountView(eventIds);
        return result.get(id);
    }

    private Map<Long, Long> getCountView(Set<Long> eventIds) {
        Long[] ids = eventIds.toArray(new Long[eventIds.size()]);
        LocalDateTime minDate = eventRepository.getMinCreatedDate(ids);
        Map<String, Long> uris = eventIds.stream().collect(Collectors.toMap((u -> "/events/" + u.toString()),
                (u -> u)));
        List<ViewStats> viewStats = client.getStatistics(
                minDate,
                LocalDateTime.now(),
                uris.keySet().toArray(new String[uris.keySet().size()]),
                false);
        Map<Long, Long> result = eventIds.stream().collect(Collectors.toMap(l -> l, l -> 0L));
        viewStats.forEach(vs -> {
            Long eventId = uris.get(vs.getUri());
            Long viewCount = vs.getHits();
            result.put(eventId, viewCount);
        });
        return result;
    }
}

