package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
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
    public Genre save(Genre genre) {
        genre.setId(getNextId());
        genres.put(genre.getId(), genre);
        return genre;
    }

    @Override
    public Genre update(Genre newGenre, Genre oldGenre) {
        oldGenre.setName(newGenre.getName());
        return oldGenre;
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        return genres.values().stream()
                .filter(genre -> genre.getId().equals(id))
                .findFirst();
    }

    private Integer getNextId() {
        int currentMaxId = genres.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
