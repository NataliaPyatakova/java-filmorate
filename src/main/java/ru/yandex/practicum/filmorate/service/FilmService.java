package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final MpaRatingService mpaRatingService;

    private static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film save(Film film) {
        log.info("Saving film {}", film);
        validateDate(film);
        dataEnrichment(film);
        return filmStorage.save(film);
    }

    public Film update(Film newFilm) {
        log.info("Updating newFilm {}", newFilm);
        validateDate(newFilm);
        dataEnrichment(newFilm);
        Film oldFilm = findById(newFilm.getId());
        log.info("Updating OldFilm {}", oldFilm);
        boolean updateName = false;  //TODO поискать решение получше
        boolean updateDescription = false;
        boolean updateReleaseDate = false;
        boolean updateDuration = false;
        if (newFilm.getName() != null && !newFilm.getName().isBlank()) { //пустоту разрешаем, но не записываем
            updateName = true;
        }
        if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()) { //пустоту разрешаем, но не записываем
            updateDescription = true;
        }
        if (newFilm.getReleaseDate() != null) { //пустоту разрешаем, но не записываем
            updateReleaseDate = true;
        }
        if (newFilm.getDuration() != 0) { //пустоту разрешаем, но не записываем
            updateDuration = true;
        }
        return filmStorage.update(newFilm, oldFilm, updateName, updateDescription, updateReleaseDate, updateDuration);
    }

    public Film findById(Integer id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    public Film addLike(Integer id, Integer userId) {
        log.info("Adding like from User {} to Film {}", userId, id);
        Film film = findById(id);
        userService.findById(userId);
        filmStorage.addLike(film, userId);
        return film;
    }

    public Film removeLike(Integer id, Integer userId) {
        log.info("Removing like from User {} to Film {}", userId, id);
        Film film = findById(id);
        userService.findById(userId);
        filmStorage.removeLike(film, userId);
        return film;
    }

    public List<Film> findMostRated(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(Film::countLikes).reversed())
                .limit(count)
                .toList();
    }

    public void deleteAll() {
        filmStorage.deleteAll();
    }

    private void validateDate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + START_RELEASE_DATE);
        }
    }

    public void dataEnrichment(Film film) {
        film.setMpa(mpaRatingService.findById(film.getMpa().getId()));
        Set<Genre> genres = film.getGenres().stream()
                .map(genre -> genreService.findById(genre.getId()))
                .collect(Collectors.toCollection(TreeSet::new));  //для правильной сортировки
        film.setGenres(genres);
    }
}
