package ru.practicum.dto.comments;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentShortDto {
    private Long id;
    private String text;
    private String nameCommentator;
    private Long eventId;
    private boolean participant;
}
