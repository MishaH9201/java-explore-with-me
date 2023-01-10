package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    public UserDto save(@RequestBody @Valid UserDtoShort userDto) {
        log.info("User {} create", userDto);
        return userService.save(userDto);
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(required = false) Long[] ids,
                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get users by ids {},from {}, size {}", Arrays.toString(ids), from, size);
        return userService.get(getPageRequest(from, size), ids);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    private static PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}
