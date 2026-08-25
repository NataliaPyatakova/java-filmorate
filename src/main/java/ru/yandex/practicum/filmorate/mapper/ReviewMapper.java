package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewReviewDto;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.dto.UpdateReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewMapper {

    public static ReviewDto mapToReviewDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(review.getReviewId());
        dto.setContent(review.getContent());
        dto.setIsPositive(review.getIsPositive());
        dto.setUserId(review.getUserId());
        dto.setFilmId(review.getFilmId());
        dto.setUseful(review.getUseful());
        return dto;
    }

    public static Review mapToReview(NewReviewDto newReviewDto) {
        Review review = new Review();
        review.setContent(newReviewDto.getContent());
        review.setIsPositive(newReviewDto.getIsPositive());
        review.setUserId(newReviewDto.getUserId());
        review.setFilmId(newReviewDto.getFilmId());
        review.setUseful(newReviewDto.getUseful());
        return review;
    }

    public static Review mapToReview(UpdateReviewDto updateReviewDto) {
        Review review = new Review();
        review.setReviewId(updateReviewDto.getReviewId());
        review.setContent(updateReviewDto.getContent());
        review.setIsPositive(updateReviewDto.getIsPositive());
        review.setUserId(updateReviewDto.getUserId());
        review.setFilmId(updateReviewDto.getFilmId());
        return review;
    }
}
