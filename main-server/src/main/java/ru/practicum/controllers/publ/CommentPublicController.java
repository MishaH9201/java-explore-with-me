package ru.practicum.controllers.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comments.CommentFullDto;
import ru.practicum.dto.comments.CommentShortDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/comments")
@Validated
@Slf4j
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentShortDto> findAll(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam long eventId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get all comments by event id {}", eventId);
        return commentService.findAllByEventId(eventId, rangeStart, rangeEnd, getPageRequest(from, size));
    }


    @GetMapping("/{id}")
    public CommentFullDto findById(@PathVariable Long id) {
        log.info("Get comment by id {}", id);
        return commentService.findById(id);
    }

    private static PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("createdOn"));
    }
}
