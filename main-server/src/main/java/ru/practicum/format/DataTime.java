package ru.practicum.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTime {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime getDataTime(String str) {
        return LocalDateTime.parse(str, formatter);
    }
}
