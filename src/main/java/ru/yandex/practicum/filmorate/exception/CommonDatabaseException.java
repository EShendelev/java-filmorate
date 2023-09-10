package ru.yandex.practicum.filmorate.exception;

public class CommonDatabaseException extends RuntimeException {
    public CommonDatabaseException() {
    }

    public CommonDatabaseException(String message) {
        super(message);
    }

    public CommonDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonDatabaseException(Throwable cause) {
        super(cause);
    }

    public CommonDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
