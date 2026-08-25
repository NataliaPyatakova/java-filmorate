package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewReviewDto;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.dto.UpdateReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto create(@Valid @RequestBody NewReviewDto newReviewDto) {
        return reviewService.create(newReviewDto);
    }

    @PutMapping
    public ReviewDto update(@Valid @RequestBody UpdateReviewDto updateReviewDto) {
        return reviewService.update(updateReviewDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public ReviewDto findFilmById(@PathVariable Integer id) {
        return reviewService.findById(id);
    }

    @GetMapping
    public List<ReviewDto> getByFilmAndCount(@RequestParam(required = false) Integer filmId,
                                             @RequestParam(defaultValue = "10") int count) {
        return reviewService.getByFilmAndCount(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDisLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addDisLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.removeLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDisLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.removeDisLike(id, userId);
    }
}
