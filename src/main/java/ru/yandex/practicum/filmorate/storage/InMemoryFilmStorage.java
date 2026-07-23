package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
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
    public Film update(Film newFilm, Film oldFilm) {
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        return oldFilm;
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

    private Integer getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
