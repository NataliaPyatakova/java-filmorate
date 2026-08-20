package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.LikesRelation;

import java.util.List;

public interface LikesRelationStorage {

    void addLike(Film film, Integer userId);

    void removeLike(Film film, Integer userId);

    Integer countLikesByFilmId(Integer id);

    List<LikesRelation> get();

}
