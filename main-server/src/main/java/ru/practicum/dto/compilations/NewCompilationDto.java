package ru.practicum.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    List<Long> events;
    Boolean pinned;
    @NotBlank
    String title;
}
