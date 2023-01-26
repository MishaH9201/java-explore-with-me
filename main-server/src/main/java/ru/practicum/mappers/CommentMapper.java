package ru.practicum.mappers;

import ru.practicum.dto.comments.CommentAdminDto;
import ru.practicum.dto.comments.CommentFullDto;
import ru.practicum.dto.comments.CommentShortDto;
import ru.practicum.dto.comments.NewCommentDto;
import ru.practicum.models.Comment;
import ru.practicum.models.Event;
import ru.practicum.models.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(NewCommentDto dto, User user, Event event) {
        return Comment.builder()
                .text(dto.getText())
                .initiator(user)
                .createdOn(LocalDateTime.now())
                .event(event)
                .stateComment(Comment.StateComment.NOT_REQUIRING)
                .moderation(false)
                .build();
    }

    public static CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .participant(comment.isParticipant())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .createdOn(comment.getCreatedOn())
                .text(comment.getText())
                .initiator(UserMapper.toUserDtoShort(comment.getInitiator()))
                .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .nameCommentator(comment.getInitiator().getName())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .participant(comment.isParticipant())
                .build();
    }

    public static CommentAdminDto toCommentAdminDto(Comment comment) {
        return CommentAdminDto.builder()
                .id(comment.getId())
                .participant(comment.isParticipant())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .createdOn(comment.getCreatedOn())
                .text(comment.getText())
                .initiator(UserMapper.toUserDtoShort(comment.getInitiator()))
                .moderation(comment.isModeration())
                .stateComment(comment.getStateComment())
                .build();
    }
}
