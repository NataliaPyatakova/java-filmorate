package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") Integer id) {
        return filmService.findById(id);
    }

    @PostMapping
    public Film save(@Validated({OnCreate.class, Default.class}) @RequestBody Film film) {
        return filmService.save(film);
    }

    @PutMapping
    public Film update(@Validated({OnUpdate.class, Default.class}) @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findMostRated(@RequestParam(value = "count", defaultValue = "10") int count) {
        return filmService.findMostRated(count);
    }
}




