package ru.practicum.service.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.dto.events.SearchEvent;
import ru.practicum.dto.events.SearchEventAdmin;
import ru.practicum.models.Event;
import ru.practicum.models.QEvent;
import ru.practicum.models.request.QRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormatForSearch {
    public static BooleanExpression formatExpression(SearchEvent filter) {

        BooleanExpression result = QEvent.event.state.eq(Event.State.PUBLISHED);
        if (filter.getText() != null) {
            result = result.and(QEvent.event.description.likeIgnoreCase(filter.getText())
                    .or(QEvent.event.annotation.likeIgnoreCase(filter.getText())));
        }

        if (filter.getCategories() != null) {

            result = result.and(QEvent.event.category.id.in(filter.getCategories()));
        }
        if (filter.getPaid() != null) {
            result = result.and(QEvent.event.paid.eq(filter.getPaid()));
        }

        if (filter.getRangeStart() == null && filter.getRangeEnd() == null) {
            result = result.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (filter.getRangeStart() != null) {
            result = result.and(QEvent.event.eventDate.after(filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            result = result.and(QEvent.event.eventDate.before(filter.getRangeEnd()));
        }
        if (filter.getOnlyAvailable() != null && filter.getOnlyAvailable()) {
            result = result.and(QRequest.request.event.count().gt(QEvent.event.participantLimit));
        }
        return result;
    }


    public static BooleanExpression formatExpression(SearchEventAdmin filter) {

        BooleanExpression result = null;
        if (filter.getCategories() != null) {
            List<Long> categories = Arrays.asList(filter.getCategories());
            result = QEvent.event.category.id.in(categories);
        }

        if (filter.getUsers() != null) {
            List<Long> users = Arrays.asList(filter.getUsers());
            if (result == null) {
                result = QEvent.event.initiator.id.in(users);
            } else {
                result.and(QEvent.event.initiator.id.in(users));
            }
        }

        if (filter.getRangeStart() != null) {
            if (result == null) {
                result = QEvent.event.eventDate.after(filter.getRangeStart());
            } else {
                result = result.and(QEvent.event.eventDate.after(filter.getRangeStart()));
            }
        }
        if (filter.getRangeEnd() != null) {
            if (result == null) {
                result = QEvent.event.eventDate.before(filter.getRangeEnd());
            } else {
                result = result.and(QEvent.event.eventDate.before(filter.getRangeEnd()));
            }
        }
        if (filter.getStates() != null) {
            List<Event.State> states = new ArrayList<>();
            for (String state : filter.getStates()) {
                states.add(Event.State.valueOf(state));
            }
            if (result == null) {
                result = QEvent.event.state.in(states);
            } else {
                result = result.and(QEvent.event.state.in(states));
            }
        }
        return result;
    }
}
