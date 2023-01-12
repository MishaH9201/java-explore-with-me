package ru.practicum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.models.Event;

import java.time.LocalDateTime;


public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Page<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    @Query(nativeQuery = true, value = "select min(created_on) from events where id in :ids")
    LocalDateTime getMinCreatedDate(Long[] ids);

}
