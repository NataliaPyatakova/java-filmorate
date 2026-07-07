package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Film save(Film film);

    Film update(Film newFilm, Film oldFilm, boolean updateName, boolean updateDescription, boolean updateReleaseDate, boolean updateDuration);

    Optional<Film> findById(Integer id);

    void addLike(Film film, Integer userId);

    void removeLike(Film film, Integer userId);

    void deleteAll();
}
