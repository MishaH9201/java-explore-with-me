package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoShort {
    @Email
    @NotBlank
    @Size(max = 512)
    private String email;
    @NotBlank
    @Size(max = 255)
    private String name;
}
