package ru.practicum.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserDtoShort;
import ru.practicum.models.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static User toUser(UserDto user) {
        return new User(user.getId(), user.getEmail(), user.getName());
    }

    public static UserDtoShort toUserDtoShort(User user) {
        return new UserDtoShort(user.getEmail(), user.getName());
    }

    public static User toUser(UserDtoShort user) {
        return new User(null, user.getEmail(), user.getName());
    }
}
