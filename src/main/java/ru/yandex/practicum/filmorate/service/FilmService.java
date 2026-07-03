package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Film findById(Integer id) {
        return filmStorage.findById(id);
    }

    public Film addLike(Integer id, Integer userId) {
        return filmStorage.addLike(id, userId);
    }

    public Film removeLike(Integer id, Integer userId) {
        return filmStorage.removeLike(id, userId);
    }

    public List<Film> findMostRated(Integer count) {
        return filmStorage.findMostRated(count);
    }
}
