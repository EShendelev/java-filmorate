package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.UserValidateFailException;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotExistException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserValidateFailException(final UserValidateFailException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final FilmNotExistException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleFilmValidateFailException(final FilmValidateFailException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }


}