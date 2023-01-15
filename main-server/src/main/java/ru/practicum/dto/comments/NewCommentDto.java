package ru.practicum.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.util.FilthyLanguage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCommentDto {
    @FilthyLanguage
    @Size(max = 280)
    @NotBlank
    private String text;
}
