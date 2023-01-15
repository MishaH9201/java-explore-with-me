package ru.practicum.dto.comments;

import lombok.*;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.models.Comment;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentAdminDto {
    private Long id;
    private String text;
    private EventShortDto event;
    private UserDtoShort initiator;
    private LocalDateTime createdOn;
    private boolean participant;
    private Comment.StateComment stateComment;
    private boolean moderation;
}
