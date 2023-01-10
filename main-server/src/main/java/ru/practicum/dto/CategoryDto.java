package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.util.Create;
import ru.practicum.util.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @NotNull(groups = Update.class)
    Long id;
    @Size(max = 255, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class, Update.class})
    private String name;
}
