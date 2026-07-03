package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private final UserService userService;

    public InMemoryFilmStorage(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @Override
    public Film save(Film film) {
        log.info("Saving film {}", film);
        validateDate(film);
        film.setId(getNextId());
        log.debug("Generated filmId {}", film.getId());
        if (film.getLikes() == null) {  //возможно сделать лучше - поискать
            Set<Integer> likes = new HashSet<>();
            film.setLikes(likes);
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Updating newFilm {}", newFilm);
        findById(newFilm.getId());
        validateDate(newFilm);
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

    @Override
    public Film findById(Integer id) {
        return films.values().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        log.info("Adding like from User {} to Film {}", userId, id);
        Film film = findById(id);
        userService.findById(userId);
        film.getLikes().add(userId);
        return film;
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        log.info("Removing like from User {} to Film {}", userId, id);
        Film film = findById(id);
        userService.findById(userId);
        film.getLikes().remove(userId);
        return film;
    }

    @Override
    public List<Film> findMostRated(Integer count) {
        return films.values().stream()
                .sorted(Comparator.comparing(Film::countLikes).reversed())
                .limit(count)
                .toList();
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
