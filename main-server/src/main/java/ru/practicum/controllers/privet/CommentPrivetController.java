package ru.practicum.controllers.privet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comments.CommentFullDto;
import ru.practicum.dto.comments.NewCommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
@Validated
@Slf4j
public class CommentPrivetController {

    private final CommentService commentService;

    @PostMapping
    public CommentFullDto add(@PathVariable long userId,
                              @PathVariable long eventId,
                              @RequestBody @Valid NewCommentDto commentDto) {
        log.trace("Create Comment user id{}.", userId);
        return commentService.add(userId, eventId, commentDto);
    }

    @GetMapping
    public List<CommentFullDto> get(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.trace("Get comment by event id {} and user id {}.", eventId, userId);
        return commentService.getByUserIdAndEventId(userId, eventId, rangeStart, rangeEnd, getPageRequest(from, size));
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto update(@PathVariable long userId,
                                 @PathVariable long eventId,
                                 @PathVariable long commentId,
                                 @RequestBody @Valid NewCommentDto commentDto) {
        log.trace("Update comment id {} and user id {}.", commentId, userId);
        return commentService.update(userId, eventId, commentId, commentDto);
    }

    @PatchMapping("/{commentId}/offensive")
    public void noteOffensive(@PathVariable long userId,
                              @PathVariable long eventId,
                              @PathVariable long commentId) {
        log.trace("User id {} flagged comment id {} as offensive.", userId, commentId);
        commentService.nodeOffensive(userId, eventId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long userId,
                       @PathVariable long commentId) {
        log.trace("Delete comment id {} and user id {}.", commentId, userId);
        commentService.delete(commentId, userId);
    }

    private static PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("createdOn"));
    }
}
