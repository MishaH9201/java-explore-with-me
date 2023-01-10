package ru.practicum.dto.events;

import lombok.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.user.UserDtoShort;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EventShortDto {
    private Long id;
    private CategoryDto category;
    private String title;
    private String annotation;
    private UserDtoShort initiator;
    private long confirmedRequests;
    private String eventDate;
    private boolean paid;
    private long views;
}
