package ru.practicum.models.request;

import lombok.Data;

@Data
public class RequestCount {
    private Long id;
    private Long count;

    public RequestCount(Long id, Long count) {
        this.id = id;
        this.count = count;
    }

}
