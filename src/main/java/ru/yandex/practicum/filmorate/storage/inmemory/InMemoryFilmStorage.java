package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @Override
    public Film save(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        return film;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        return films.values().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst();
    }

    @Override
    public void addLike(Film film, Integer userId) {
        film.getLikes().add(userId);
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        film.getLikes().remove(userId);
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

    @Override
    public Integer countLikesByFilmId(Integer id) {
        return 0;
    }

    private Integer getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
