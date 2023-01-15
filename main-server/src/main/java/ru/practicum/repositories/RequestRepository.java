package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.request.Request;
import ru.practicum.models.request.RequestCount;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(long userId);

    Request findByEvent_IdAndRequester_Id(long eventId, long userId);

    @Query("select count(request) " +
            "from Request request " +
            "where request.event.id = ?1 and request.state = 'CONFIRMED'")
    long getConfirmedRequestsAmount(long eventId);

    @Query("SELECT new ru.practicum.models.request.RequestCount(c.event.id, COUNT(c.event.id)) " +
            "FROM Request AS c " +
            "WHERE c.state = 'CONFIRMED' AND c.event.id IN :eventIds " +
            "GROUP BY c.event.id ORDER BY c.event.id DESC")
    List<RequestCount> getConfirmedRequestsAmount(List<Long> eventIds);

    Optional<Request> findByIdAndRequester_Id(long requestId, long requesterId);

    List<Request> findAllByEvent_Id(Long eventId);

    List<Request> findAllByEvent_IdAndState(Long eventId, Request.State state);

    boolean existsRequestByRequesterIdAndState(Long userId, Request.State state);

}

