package ru.practicum.service.comment;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.comments.*;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;
import ru.practicum.models.request.Request;
import ru.practicum.repositories.CommentRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.RequestRepository;
import ru.practicum.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public CommentFullDto add(long userId, long eventId, NewCommentDto commentDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Comment comment = repository.save(CommentMapper.toComment(commentDto, user, event));
        comment.setParticipant(requestRepository.existsRequestByRequesterIdAndState(userId, Request.State.CONFIRMED));
        return CommentMapper.toCommentFullDto(comment);
    }

    public List<CommentFullDto> getByUserIdAndEventId(long userId, long eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest) {
        eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        Iterable<Comment> commentIterable = repository.findAll(FormatForSearchComment.getFiler(userId, eventId, rangeStart, rangeEnd), pageRequest);
        List<Comment> comments = Lists.newArrayList(commentIterable);
        return comments.stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public CommentFullDto update(long userId, long eventId, long commentId, NewCommentDto commentDto) {
        Comment comment = repository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if (comment.getEvent().getId() != eventId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event does not match request");
        }
        if (comment.getInitiator().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the owner can edit the comment");
        }
        comment.setText(commentDto.getText());
        return CommentMapper.toCommentFullDto(comment);
    }

    @Transactional
    public void nodeOffensive(long userId, long eventId, long commentId) {
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Comment comment = repository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if (comment.getEvent().getId() != eventId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event does not match request");
        }
        if (comment.isModeration()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment already checked");
        }
        comment.setStateComment(Comment.StateComment.PENDING);
    }

    public CommentFullDto findById(Long id) {
        Comment comment = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        return CommentMapper.toCommentFullDto(comment);
    }

    public List<CommentShortDto> findAllByEventId(long eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest) {
        eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        Iterable<Comment> commentIterable = repository.findAll(FormatForSearchComment.getFiler(null, eventId, rangeStart, rangeEnd), pageRequest);
        List<Comment> comments = Lists.newArrayList(commentIterable);
        return comments.stream()
                .map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(long commentId, long userId) {
        Comment comment = repository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if (comment.getInitiator().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the owner can delete the comment");
        }
        repository.deleteById(commentId);
    }

    @Transactional
    public CommentAdminDto moderation(long commentId) {
        Comment comment = repository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        comment.setStateComment(Comment.StateComment.NOT_REQUIRING);
        comment.setModeration(true);
        return CommentMapper.toCommentAdminDto(comment);
    }

    @Transactional
    public void deleteAdmin(long commentId) {
        repository.deleteById(commentId);
    }

    public List<CommentAdminDto> findAll(SearchComment searchComment, PageRequest pageRequest) {
        BooleanExpression filter = FormatForSearchComment.getFiler(searchComment);
        Iterable<Comment> commentIterable = repository.findAll(filter, pageRequest);
        List<Comment> comments = Lists.newArrayList(commentIterable);
        return comments.stream()
                .map(CommentMapper::toCommentAdminDto)
                .collect(Collectors.toList());
    }
}


