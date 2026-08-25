package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review create(Review review);

    Review update(Review review);

    void delete(Integer reviewId);

    Optional<Review> findById(Integer reviewId);

    List<Review> getByFilmAndCount(Integer filmId, int count);

    List<Review> getByCount(int count);

    void addLike(Integer reviewId, Integer userId);

    void addDisLike(Integer reviewId, Integer userId);

    void removeLike(Integer reviewId, Integer userId);

    void removeDisLike(Integer reviewId, Integer userId);
}
