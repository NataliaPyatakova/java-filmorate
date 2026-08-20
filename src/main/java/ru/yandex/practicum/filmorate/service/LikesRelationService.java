package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.LikesRelation;
import ru.yandex.practicum.filmorate.storage.LikesRelationStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikesRelationService {

    private final LikesRelationStorage likesRelationStorage;

    public void addLike(Film film, Integer userId) {
        likesRelationStorage.addLike(film, userId);
    }

    public void removeLike(Film film, Integer userId) {
        likesRelationStorage.removeLike(film, userId);
    }

    public Integer countLikesByFilmId(Integer id) {
        return likesRelationStorage.countLikesByFilmId(id);
    }

    public List<LikesRelation> get() {
        return likesRelationStorage.get();
    }
}
