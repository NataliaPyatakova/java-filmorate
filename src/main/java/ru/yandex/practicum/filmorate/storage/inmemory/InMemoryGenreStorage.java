package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.*;

@Component
@Qualifier("InMemoryGenreStorage")
public class InMemoryGenreStorage implements GenreStorage {

    private final Map<Integer, Genre> genres = new HashMap<>();

    public InMemoryGenreStorage() {
        genres.clear();
        genres.put(1, new Genre().setId(1).setName("Комедия"));
        genres.put(2, new Genre().setId(2).setName("Драма"));
        genres.put(3, new Genre().setId(3).setName("Мультфильм"));
        genres.put(4, new Genre().setId(4).setName("Триллер"));
        genres.put(5, new Genre().setId(5).setName("Документальный"));
        genres.put(6, new Genre().setId(6).setName("Боевик"));
    }

    @Override
    public List<Genre> findAll() {
        return genres.values().stream().toList();
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        return genres.values().stream()
                .filter(genre -> genre.getId().equals(id))
                .findFirst();
    }

    @Override
    public Set<Genre> findByFilmId(Integer id) {
        return Set.of();
    }
}
