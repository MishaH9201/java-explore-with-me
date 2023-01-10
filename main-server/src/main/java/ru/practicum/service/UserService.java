package ru.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.mappers.UserMapper;
import ru.practicum.models.User;
import ru.practicum.repositories.UserRepository;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    @Transactional
    public UserDto save(UserDtoShort userDtoShort) {
        User user = UserMapper.toUser(userDtoShort);
        try {
            return UserMapper.toUserDto(repository.save(user));
        } catch (RuntimeException e) {
            Throwable rootCause = com.google.common.base.Throwables.getRootCause(e);
            if (rootCause instanceof SQLException) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already in use");
            } else {
                throw e;
            }
        }
    }

    public List<UserDto> get(PageRequest pageRequest, Long[] ids) {
        if (ids == null) {
            return repository.findAll(pageRequest).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllById(Arrays.asList(ids)).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
