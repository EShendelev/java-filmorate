package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@RequestBody @Valid Review review) {
        return reviewService.addOrUpdateReview(review, true);
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review) {
        return reviewService.addOrUpdateReview(review, false);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable int reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping
    public Collection<Review> getAllReviews() {
        log.info("Вывод всех отзывов");
        return reviewService.getAllReviews();
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLikeToReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.addMarkToReview(userId, reviewId, true);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.addMarkToReview(userId, reviewId, false);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLikeFromReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.removeMarkFromReview(userId, reviewId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislikeFromReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.removeMarkFromReview(userId, reviewId);
    }

    @GetMapping(params = {"filmId"})
    public Collection<Review> getReviewsByFilmId(@RequestParam long filmId,
                                                 @RequestParam(required = false) String count) {
        return reviewService.getReviewsByFilmId(filmId, count);
    }
}
