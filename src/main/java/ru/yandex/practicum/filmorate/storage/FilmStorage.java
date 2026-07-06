package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film save(Film film);

    Film update(Film newFilm);

    Film findById(Integer id);

    Film addLike(Integer id, Integer userId);

    Film removeLike(Integer id, Integer userId);

    List<Film> findMostRated(Integer count);

    void deleteAll();
}
