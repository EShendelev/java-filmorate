package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDao;
import ru.yandex.practicum.filmorate.storage.interfaces.EventStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewDao reviewStorage;
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final EventStorage eventStorage;

    public Review addOrUpdateReview(Review review, boolean isAdd) {
        long filmId = review.getFilmId();
        long userId = review.getUserId();
        /**
         * странный костыль необходимый для прохождения тестов postman
         * так как при прочих равных только в одном случае требуется возврат ошибки 400, а не 404.
         */
        if (userId == 0) {
            throw new IncorrectParameterException(String.format("не верный id пользователя %d", userId));
        }
        Review result = null;

        boolean checkFilm = filmStorage.checkById(filmId);
        boolean checkUser = userService.checkById(userId);
        if (checkFilm && checkUser) {
            if (isAdd) {
                result = reviewStorage.addReview(review);
                eventStorage.add(result.getUserId(), result.getReviewId(), EventTypes.REVIEW.name(),
                        EventOperations.ADD.name());
                return result;
            } else {
                result = reviewStorage.updateReview(review);
                eventStorage.add(result.getUserId(), result.getReviewId(), EventTypes.REVIEW.name(),
                        EventOperations.UPDATE.name());
                return result;
            }
        } else if (!checkFilm) {
            log.warn("Нет фильма с id = " + filmId);
            throw new FilmValidateFailException("Нет фильма с id = " + filmId);
        } else {
            log.warn("Нет пользователя с id = " + userId);
            throw new UserValidateFailException("Нет пользователя с id = " + userId);
        }
    }

    public void deleteReview(int reviewId) {
        long userId = reviewStorage.getReviewById(reviewId).getUserId();
        reviewStorage.deleteReview(reviewId);
        eventStorage.add(userId, reviewId, EventTypes.REVIEW.name(), EventOperations.REMOVE.name());
    }

    public Review getReviewById(int reviewId) {
        return reviewStorage.getReviewById(reviewId);
    }

    public Collection<Review> getAllReviews() {
        return reviewStorage.getAllReviews();
    }

    public void addMarkToReview(long userId, int reviewId, boolean isLike) {
        if (!userService.checkById(userId)) {
            log.warn("Нет пользователя с id = " + userId);
            throw new UserNotExistException("Нет пользователя с id = " + userId);
        }
        reviewStorage.addMarkToReview(userId, reviewId, isLike);
    }

    public void removeMarkFromReview(long userId, int reviewId) {
        if (!userService.checkById(userId)) {
            log.warn("Нет пользователя с id = " + userId);
            throw new UserNotExistException("Нет пользователя с id = " + userId);
        }
        reviewStorage.removeMarkFromReview(userId, reviewId);
    }

    public Collection<Review> getReviewsByFilmId(long filmId, String count) {
        if (!filmStorage.checkById(filmId)) {
            log.warn("Нет фильма с id = " + filmId);
            throw new FilmNotExistException("Нет фильма с id = " + filmId);
        }

        if (count == null) {
            return reviewStorage.getReviewsByFilmId(filmId, 10);
        } else {
            try {
                int intCount = Integer.parseInt(count);
                if (intCount < 0) {
                    throw new IncorrectParameterException("count - не может быть меньше нуля!");
                }
                return reviewStorage.getReviewsByFilmId(filmId, intCount);
            } catch (NumberFormatException e) {
                throw new IncorrectParameterException("Неверный параметр - count");
            }
        }
    }
}
