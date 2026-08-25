package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewReviewDto;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.dto.UpdateReviewDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.enumeration.EventType;
import ru.yandex.practicum.filmorate.enumeration.Operation;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmService filmService;
    private final UserService userService;
    private final EventService eventService;

    public ReviewDto create(NewReviewDto newReviewDto) {
        log.info("Сохраняем review {}", newReviewDto);
        filmService.findById(newReviewDto.getFilmId());
        userService.findById(newReviewDto.getUserId());
        Review newReview = reviewStorage.create(ReviewMapper.mapToReview(newReviewDto));
        eventService.createEvent(newReview.getUserId(), EventType.REVIEW, Operation.ADD, newReview.getReviewId());
        return ReviewMapper.mapToReviewDto(newReview);
    }

    public ReviewDto update(UpdateReviewDto updateReviewDto) {
        log.info("Обновляем reviewId = {}", updateReviewDto.getReviewId());
        findById(updateReviewDto.getReviewId());
        filmService.findById(updateReviewDto.getFilmId());
        userService.findById(updateReviewDto.getUserId());
        Review newReview = reviewStorage.update(ReviewMapper.mapToReview(updateReviewDto));
        eventService.createEvent(newReview.getUserId(), EventType.REVIEW, Operation.UPDATE, newReview.getReviewId());
        return ReviewMapper.mapToReviewDto(newReview);
    }

    public void delete(Integer reviewId) {
        ReviewDto review = findById(reviewId);
        eventService.createEvent(review.getUserId(), EventType.REVIEW, Operation.REMOVE, reviewId);
        reviewStorage.delete(reviewId);
    }

    public ReviewDto findById(Integer reviewId) {
        Review foundedReview = reviewStorage.findById(reviewId).orElseThrow(() -> new NotFoundException("Отзыв с id = " + reviewId + " не найден"));
        return ReviewMapper.mapToReviewDto(foundedReview);
    }

    public List<ReviewDto> getByFilmAndCount(Integer filmId, int count) {
        if (filmId == null) {
            return reviewStorage.getByCount(count).stream().map(ReviewMapper::mapToReviewDto).toList();
        }
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
