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
    public UserDto saveNewUser(@RequestBody @Valid UserDtoShort userDto) {
        log.info("User {} create", userDto);
        return userService.saveUser(userDto);
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(required = false) Long[] ids,
            @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get users by ids {},from {}, size {}", Arrays.toString(ids), from, size);
        return userService.getUsers(getPageRequest(from, size),ids);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}
