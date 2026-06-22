package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @PostMapping
    public Film save(@Validated({OnCreate.class, Default.class}) @RequestBody Film film) {
        log.info("Saving film {}", film);
        validateDate(film);
        film.setId(getNextId());
        log.debug("Generated filmId {}", film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Validated({OnUpdate.class, Default.class}) @RequestBody Film newFilm) {
        log.info("Updating newFilm {}", newFilm);
        validateDate(newFilm);
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.info("Updating OldFilm {}", oldFilm);
            if (newFilm.getName() != null && !newFilm.getName().isBlank()) { //пустоту разрешаем, но не записываем
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()) { //пустоту разрешаем, но не записываем
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null) { //пустоту разрешаем, но не записываем
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        }
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private static void validateDate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + START_RELEASE_DATE);
        }
    }

    private Integer getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}