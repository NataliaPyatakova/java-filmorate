package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.enumeration.EventType;
import ru.yandex.practicum.filmorate.enumeration.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresRelation;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreService genreService;
    private final MpaRatingService mpaRatingService;
    private final Map<Integer, MpaRating> mpaRatingList;
    private final LikesRelationService likesRelationService;
    private final EventService eventService;

    private static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage,
                       GenreService genreService,
                       MpaRatingService mpaRatingService,
                       LikesRelationService likesRelationService,
                       EventService eventService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreService = genreService;
        this.mpaRatingService = mpaRatingService;
        this.mpaRatingList = mpaRatingService.findAll().stream()
                .map(MpaRatingMapper::mapToMpaRating)
                .collect(Collectors.toMap(MpaRating::getId, MpaRating -> MpaRating));
        this.likesRelationService = likesRelationService;
        this.eventService = eventService;
    }

    public List<FilmDto> findAll() {
        List<Film> films = filmStorage.findAll();
        dataEnrichment(films);
        return films.stream().map(FilmMapper::mapToFilmDto).toList();
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
        checkUserExist(id);
        likesRelationService.addLike(film, userId);
        dataEnrichment(film);
        eventService.createEvent(userId, EventType.LIKE, Operation.ADD, id);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto removeLike(Integer id, Integer userId) {
        log.info("Removing like from User {} to Film {}", userId, id);
        Film film = findFilmById(id);
        checkUserExist(id);
        likesRelationService.removeLike(film, userId);
        dataEnrichment(film);
        eventService.createEvent(userId, EventType.LIKE, Operation.REMOVE, id);
        return FilmMapper.mapToFilmDto(film);
    }

    public List<FilmDto> findMostRated(Integer count, Integer genreId, Integer year) {
        log.info("findMostRated count {}, genreId {}, year {}", count, genreId, year);
        if (year != null && year < START_RELEASE_DATE.getYear()) {
            throw new ValidationException("Год не может быть раньше " + START_RELEASE_DATE.getYear());
        }
        if (genreId != null && genreId < 0) {
            mpaRatingService.findMpaRatingById(genreId);
        }
        List<Film> films = filmStorage.getPopular(count, genreId, year);
        dataEnrichment(films);
        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public List<FilmDto> getByIds(Set<Integer> filmIds) {
        List<Film> films = filmStorage.getByIds(filmIds);
        dataEnrichment(films);
        return films.stream().map(FilmMapper::mapToFilmDto).toList();
    }

    public void deleteFilm(Integer id) {
        log.info("Deleting film {}", id);
        filmStorage.deleteById(id);
    }

    public List<FilmDto> getCommonFilms(Integer userId, Integer friendId) {
        checkUserExist(userId);
        checkUserExist(friendId);
        List<Film> films = filmStorage.getCommonFilms(userId, friendId);
        dataEnrichment(films);
        return films.stream().map(FilmMapper::mapToFilmDto).toList();
    }

    private void checkUserExist(Integer userId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private Film findFilmById(Integer id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + START_RELEASE_DATE);
        }
        //не трогать - запуск единичный, нужна проверка на существование id
        mpaRatingService.findById((film.getMpa().getId()));
        film.getGenres().forEach(genre -> genreService.findById(genre.getId()));
    }

    private void dataEnrichment(Film film) {
        film.setMpa(mpaRatingList.get(film.getMpa().getId()));
        film.setGenres(genreService.findByFilmId(film.getId()));
    }

    private void dataEnrichment(List<Film> films) {
        List<GenresRelation> genreList = genreService.findFilmGenreRelations();
        Map<Integer, Set<Genre>> genreMap = new HashMap<>();
        genreList.forEach(genresRelation -> {
                    Set<Genre> genreSet1 = genreMap.computeIfAbsent(genresRelation.getId(), g -> new TreeSet<>());
                    genreSet1.add(genresRelation.getGenre());
                }
        );
        films.forEach(film -> film.setMpa(mpaRatingList.get(film.getMpa().getId())));
        films.forEach(film -> film.setGenres(genreMap.computeIfAbsent(film.getId(), g -> new TreeSet<>())));
    }
}

