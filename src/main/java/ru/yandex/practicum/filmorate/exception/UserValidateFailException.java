package ru.yandex.practicum.filmorate.exception;

public class UserValidateFailException extends RuntimeException {
    public UserValidateFailException(String s) {
        super(s);
    }
}
