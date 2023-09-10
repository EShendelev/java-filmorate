package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<Director> getDirectors() {
        log.info("Вывод всех режиссеров");
        return directorService.getDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable @Positive int id) {
        log.info("Вывод режиссера id {}", id);
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public Director addDirector(@RequestBody @Valid Director director) {
        Director directorAdded = directorService.addDirector(director);
        log.info(String.format("Режиссер \"%s\" добавлен, c id = \"%s\"", directorAdded.getName(), directorAdded.getId()));
        return directorAdded;
    }

    @PutMapping
    public Director updateDirector(@RequestBody @Valid Director director) {
        Director directorUpdated = directorService.updateDirector(director);
        log.info(String.format("Режиссер id %s обновлен", directorUpdated.getId()));
        return directorUpdated;
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable @Positive int id) {
        directorService.deleteDirector(id);
        log.info(String.format("Режиссер id %s удален", id));
    }
}
