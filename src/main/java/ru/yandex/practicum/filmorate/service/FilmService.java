package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
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

    public List<FilmDto> findAll() {
        return filmStorage.findAll().stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public FilmDto save(NewFilmDto newFilmDto) {
        log.info("Saving film {}", newFilmDto);
        Film film = FilmMapper.mapToFilm(newFilmDto);
        validateDate(film);
        dataEnrichment(film);
        Film newFilm = filmStorage.save(film);
        return FilmMapper.mapToFilmDto(newFilm);
    }

    public FilmDto update(UpdateFilmDto updateFilmDto) {
        log.info("Updating newFilm {}", updateFilmDto);
        Film oldFilm = findFilmById(updateFilmDto.getId());
        log.info("Updating OldFilm {}", oldFilm);
        Film newFilm = FilmMapper.updateFilmFields(oldFilm, updateFilmDto);
        validateDate(newFilm);
        dataEnrichment(newFilm);
        Film updatedFilm = filmStorage.update(newFilm, oldFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public FilmDto findById(Integer id) {
        return FilmMapper.mapToFilmDto(findFilmById(id));
    }

    public FilmDto addLike(Integer id, Integer userId) {
        log.info("Adding like from User {} to Film {}", userId, id);
        Film film = findFilmById(id);
        userService.findById(userId);
        filmStorage.addLike(film, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto removeLike(Integer id, Integer userId) {
        log.info("Removing like from User {} to Film {}", userId, id);
        Film film = findFilmById(id);
        userService.findById(userId);
        filmStorage.removeLike(film, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public List<FilmDto> findMostRated(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(Film::countLikes).reversed())
                .limit(count)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public void deleteAll() {
        filmStorage.deleteAll();
    }

    private Film findFilmById(Integer id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    private void validateDate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + START_RELEASE_DATE);
        }
    }

    public void dataEnrichment(Film film) {
        film.setMpa(mpaRatingService.findMpaRatingById((film.getMpa().getId())));
        Set<Genre> genres = film.getGenres().stream()
                .map(genre -> genreService.findGenreById(genre.getId()))
                .collect(Collectors.toCollection(TreeSet::new));  //для правильной сортировки
        film.setGenres(genres);
    }
}
