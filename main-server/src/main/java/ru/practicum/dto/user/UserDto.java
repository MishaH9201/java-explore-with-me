package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    private String name;
}
