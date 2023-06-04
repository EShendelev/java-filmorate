package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Collection<Film> findAll() {
        return null;
    }

    @Override
    public Film add(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film delete(Long id) {
        return null;
    }

    @Override
    public Film findById(Long id) {
        return null;
    }


}
