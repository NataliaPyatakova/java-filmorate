package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> findAll();

    Genre save(Genre genre);

    Genre update(Genre newGenre, Genre oldGenre);

    Optional<Genre> findById(Integer id);
}
