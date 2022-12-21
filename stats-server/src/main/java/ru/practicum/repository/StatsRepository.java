package ru.practicum.repository;

import ru.practicum.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StatsRepository extends JpaRepository<EndpointHit,Long>, QuerydslPredicateExecutor<EndpointHit> {
}
