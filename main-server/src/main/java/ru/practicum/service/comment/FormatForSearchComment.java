package ru.practicum.service.comment;

import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.dto.comments.SearchComment;
import ru.practicum.models.Comment;
import ru.practicum.models.QComment;

import java.time.LocalDateTime;


public class FormatForSearchComment {
    public static BooleanExpression getFiler(Long userId, long eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        BooleanExpression filter;
        filter = QComment.comment.event.id.in(eventId);
        if (userId != null) {
            filter = filter.and(QComment.comment.initiator.id.in(userId));
        }
        if (rangeStart != null) {
            filter = filter.and(QComment.comment.createdOn.after(rangeStart));
        }
        if (rangeEnd != null) {
            filter = filter.and(QComment.comment.createdOn.before(rangeEnd));
        }
        return filter;
    }

    public static BooleanExpression getFiler(SearchComment searchComment) {
        BooleanExpression filter = null;
        if (searchComment.getEvents() != null) {
            filter = QComment.comment.event.id.in(searchComment.getEvents());
        }
        if (searchComment.getUsers() != null) {
            if (filter == null) {
                filter = QComment.comment.initiator.id.in(searchComment.getUsers());
            } else {
                filter = filter.and(QComment.comment.initiator.id.in(searchComment.getUsers()));
            }
        }
        if (searchComment.getState() != null) {
            if (filter == null) {
                filter = QComment.comment.stateComment.eq(Comment.StateComment.valueOf(searchComment.getState()));
            } else {
                filter = filter.and(QComment.comment.stateComment.eq(Comment.StateComment.valueOf(searchComment.getState())));
            }
        }
        if (searchComment.getRangeStart() != null) {
            if (filter == null) {
                filter = QComment.comment.createdOn.after(searchComment.getRangeStart());
            } else {
                filter = filter.and(QComment.comment.createdOn.after(searchComment.getRangeStart()));
            }
        }
        if (searchComment.getRangeEnd() != null) {
            if (filter == null) {
                filter = QComment.comment.createdOn.before(searchComment.getRangeEnd());
            } else {
                filter = filter.and(QComment.comment.createdOn.before(searchComment.getRangeEnd()));
            }
        }
        return filter;
    }
}
