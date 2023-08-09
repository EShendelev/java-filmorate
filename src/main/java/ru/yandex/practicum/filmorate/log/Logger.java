package ru.yandex.practicum.filmorate.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

@Slf4j
public class Logger {
    public static void logRequest(HttpMethod method, String uri, String body) {
        log.info("Request: '{} {}'. Request body: '{}'", method, uri, body);
    }

    public static void logInfo(String info) {
        log.info(info);
    }

}
