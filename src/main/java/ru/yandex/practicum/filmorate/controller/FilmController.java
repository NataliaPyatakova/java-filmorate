package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.enumeration.FilmByField;
import ru.yandex.practicum.filmorate.enumeration.FilmSortField;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto findById(@PathVariable("id") @NotNull Integer id) {
        return filmService.findById(id);
    }

    @PostMapping
    public FilmDto save(@Validated @RequestBody NewFilmDto newFilmDto) {
        return filmService.save(newFilmDto);
    }

    @PutMapping
    public FilmDto update(@Validated @RequestBody UpdateFilmDto updateFilmDto) {
        return filmService.update(updateFilmDto);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable("id") @NotNull Integer id, @PathVariable("userId") @NotNull Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto removeLike(@PathVariable("id") @NotNull Integer id, @PathVariable("userId") @NotNull Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> findMostRated(@RequestParam(value = "count", defaultValue = "10") @Positive int count,
                                       @RequestParam(required = false)@Positive Integer genreId,
                                       @RequestParam(required = false) Integer year) {
        return filmService.findMostRated(count, genreId, year);
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
    }

    @GetMapping("/common")
    public List<FilmDto> getCommonFilms(@RequestParam @NotNull Integer userId, @RequestParam @NotNull Integer friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<FilmDto> findByDirector(@PathVariable @NotNull Integer directorId,
                                        @RequestParam List<FilmSortField> sortBy) {
        return filmService.findByDirector(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<FilmDto> search(@RequestParam String query, @RequestParam List<FilmByField> by) {
        return filmService.search(query,by);
    }
}




