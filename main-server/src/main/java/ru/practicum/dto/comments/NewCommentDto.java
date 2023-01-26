package ru.practicum.dto.comments;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.util.FilthyLanguage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentDto {
    @FilthyLanguage
    @Size(max = 280)
    @NotBlank
    String text;
}
