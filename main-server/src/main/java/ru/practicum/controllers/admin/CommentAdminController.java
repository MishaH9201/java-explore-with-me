package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comments.CommentAdminDto;
import ru.practicum.dto.comments.SearchComment;
import ru.practicum.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentAdminDto moderation(@PathVariable long commentId) {
        log.trace("Update comment id {}.", commentId);
        return commentService.moderation(commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteAdmin(@PathVariable long commentId) {
        log.trace("Update comment id {}.", commentId);
        commentService.deleteAdmin(commentId);
    }

    @GetMapping
    public List<CommentAdminDto> findEvents(@RequestParam(required = false) Long[] users,
                                            @RequestParam(required = false) String state,
                                            @RequestParam(required = false) Long[] events,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @Positive @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        SearchComment searchComment = SearchComment.builder()
                .events(events)
                .users(users)
                .state(state)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        return commentService.findAll(searchComment, getPageRequest(from, size));
    }


    private static PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}
