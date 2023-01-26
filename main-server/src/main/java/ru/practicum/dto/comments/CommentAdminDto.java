package ru.practicum.dto.comments;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.models.Comment;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentAdminDto {
    Long id;
    String text;
    EventShortDto event;
    UserDtoShort initiator;
    LocalDateTime createdOn;
    boolean participant;
    Comment.StateComment stateComment;
    boolean moderation;
}
