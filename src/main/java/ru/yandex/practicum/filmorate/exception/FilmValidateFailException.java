package ru.yandex.practicum.filmorate.exception;

public class FilmValidateFailException extends RuntimeException {
    public FilmValidateFailException(String s) {
        super(s);
    }
}
