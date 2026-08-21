package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewReviewDto;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.dto.UpdateReviewDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, FilmService filmService, UserService userService) {
        this.reviewStorage = reviewStorage;
        this.filmService = filmService;
        this.userService = userService;
    }

    public ReviewDto create(NewReviewDto newReviewDto) {
        log.info("Сохраняем review {}", newReviewDto);
        filmService.findById(newReviewDto.getFilmId());
        userService.findById(newReviewDto.getUserId());
        Review newReview = reviewStorage.create(ReviewMapper.mapToReview(newReviewDto));
        return ReviewMapper.mapToReviewDto(newReview);
    }

    public ReviewDto update(UpdateReviewDto updateReviewDto) {
        log.info("Обновляем reviewId = {}", updateReviewDto.getReviewId());
        findById(updateReviewDto.getReviewId());
        filmService.findById(updateReviewDto.getFilmId());
        userService.findById(updateReviewDto.getUserId());
        Review newReview = reviewStorage.update(ReviewMapper.mapToReview(updateReviewDto));
        return ReviewMapper.mapToReviewDto(newReview);
    }

    public void delete(Integer reviewId) {
        reviewStorage.delete(reviewId);
    }

    public ReviewDto findById(Integer reviewId) {
        Review foundedReview = reviewStorage.findById(reviewId).orElseThrow(() -> new NotFoundException("Отзыв с id = " + reviewId + " не найден"));
        return ReviewMapper.mapToReviewDto(foundedReview);
    }

    public List<ReviewDto> getByFilmAndCount(Integer filmId, int count) {
        filmService.findById(filmId);
        return reviewStorage.getByFilmAndCount(filmId, count).stream().map(ReviewMapper::mapToReviewDto).toList();
    }

    public void addLike(Integer reviewId, Integer userId) {
        log.info("Ставим like на reviewId = {} от userId = {}", reviewId, userId);
        findById(reviewId);
        userService.findById(userId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDisLike(Integer reviewId, Integer userId) {
        log.info("Ставим dislike на reviewId = {} от userId = {}", reviewId, userId);
        findById(reviewId);
        userService.findById(userId);
        reviewStorage.addDisLike(reviewId, userId);
    }

    public void removeLike(Integer reviewId, Integer userId) {
        log.info("Убираем like на reviewId = {} от userId = {}", reviewId, userId);
        findById(reviewId);
        userService.findById(userId);
        reviewStorage.removeLike(reviewId, userId);
    }

    public void removeDisLike(Integer reviewId, Integer userId) {
        log.info("Убираем dislike на reviewId = {} от userId = {}", reviewId, userId);
        findById(reviewId);
        userService.findById(userId);
        reviewStorage.removeDisLike(reviewId, userId);
    }
}
