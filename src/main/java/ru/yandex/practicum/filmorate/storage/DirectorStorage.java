package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.DirectorsRelation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DirectorStorage {

    List<Director> findAll();

    Optional<Director> findDirectorById(Integer id);

    Director create(Director director);

    Director update(Director director);

    void delete(Integer directorId);

    Set<Director> findDirectorByFilmId(Integer filmId);

    List<DirectorsRelation> findDirectorsRelations();
}
