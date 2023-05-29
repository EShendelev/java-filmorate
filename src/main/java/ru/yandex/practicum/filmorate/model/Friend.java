package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Friend {
    private Long userId;
    private Long friendId;
    private Character statusCode;
    private LocalDateTime specifiedDateTime;
    private Long specifierId;
}
