package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.mappers.UserMapper;
import ru.practicum.models.User;
import ru.practicum.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public UserDto saveUser(UserDtoShort userDtoShort) {
        if (repository.findByName(userDtoShort.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already in use");
        }
        User user = UserMapper.toUser(userDtoShort);
        return UserMapper.toUserDto(repository.save(user));
    }

    public List<UserDto> getUsers(PageRequest pageRequest, Long[] ids) {
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

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
