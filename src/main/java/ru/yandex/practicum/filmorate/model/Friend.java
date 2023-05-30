package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Friend {
    private Long userId;
    private Long friendId;
    private Character statusCode;
    private LocalDateTime specifiedDateTime;
    private Long specifierId;
}
