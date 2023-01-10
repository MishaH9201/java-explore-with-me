package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private String created;
    private String status;
}
