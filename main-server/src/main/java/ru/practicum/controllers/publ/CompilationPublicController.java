package ru.practicum.controllers.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get compilations by from {}, size {}", from, size);
        return service.getAll(pinned, getPageRequest(from, size));
    }


    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        log.trace("Get compilations by id {}.", compId);
        return service.getById(compId);
    }

    private static PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}
