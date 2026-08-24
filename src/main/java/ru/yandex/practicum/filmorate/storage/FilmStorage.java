package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    List<Film> findAll();

    Film save(Film film);

    Film update(Film film);

    Optional<Film> findById(Integer id);

    void deleteAll();

    List<Film> getByIds(Set<Integer> filmIds);

    void deleteById(Integer id);

    List<Film> getPopular(int count, Integer genreId, Integer year);

    List<Film> getCommonFilms(Integer userId, Integer friendId);
}
