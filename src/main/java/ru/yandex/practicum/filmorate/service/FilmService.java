package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final MpaRatingService mpaRatingService;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService, GenreService genreService, MpaRatingService mpaRatingService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreService = genreService;
        this.mpaRatingService = mpaRatingService;
    }

    private static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public List<FilmDto> findAll() {
        return filmStorage.findAll().stream()
                .peek(this::dataEnrichment)
                .map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public FilmDto save(NewFilmDto newFilmDto) {
        log.info("Saving film {}", newFilmDto);
        Film film = FilmMapper.mapToFilm(newFilmDto);
        validate(film);
        Film newFilm = filmStorage.save(film);
        dataEnrichment(newFilm);
        return FilmMapper.mapToFilmDto(newFilm);
    }

    public FilmDto update(UpdateFilmDto updateFilmDto) {
        log.info("Updating newFilm {}", updateFilmDto);
        Film oldFilm = findFilmById(updateFilmDto.getId());
        log.info("Updating OldFilm {}", oldFilm);
        Film newFilm = FilmMapper.updateFilmFields(oldFilm, updateFilmDto);
        validate(newFilm);
        dataEnrichment(newFilm);
        Film updatedFilm = filmStorage.update(newFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public FilmDto findById(Integer id) {
        Film film = findFilmById(id);
        dataEnrichment(film);
        return FilmMapper.mapToFilmDto(film);
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
                .peek(film -> film.setCountLikes(filmStorage.countLikesByFilmId(film.getId())))
                .sorted(Comparator.comparing(Film::getCountLikes).reversed())
                .limit(count)
                .peek(this::dataEnrichment)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    private Film findFilmById(Integer id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + START_RELEASE_DATE);
        }
        mpaRatingService.findMpaRatingById((film.getMpa().getId()));
        film.getGenres().forEach(genre -> genreService.findById(genre.getId()));
    }

    public void dataEnrichment(Film film) {
        film.setMpa(mpaRatingService.findMpaRatingById((film.getMpa().getId())));
        film.setGenres(genreService.findByFilmId(film.getId()));
    }
}
