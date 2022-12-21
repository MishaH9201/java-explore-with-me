package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(long userId);

    Request findByEvent_IdAndRequester_Id(long eventId, long userId);

    @Query("select count(request) " +
            "from Request request " +
            "where request.event.id = ?1 and request.state = 'CONFIRMED'")
    int getConfirmedRequestsAmount(long eventId);

    Request findByIdAndRequester_Id(long requestId, long requesterId);

    List<Request> findAllByEvent_Id(Long eventId);

}

