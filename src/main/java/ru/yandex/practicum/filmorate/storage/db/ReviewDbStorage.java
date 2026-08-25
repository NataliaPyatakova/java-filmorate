package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDbStorage extends BaseStorage<Review> implements ReviewStorage {

    private static final String INSERT_QUERY = """
            INSERT INTO reviews(content, is_positive, user_id, film_id, useful)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_QUERY = """
            UPDATE reviews
            SET content = ?, is_positive = ?
            WHERE review_id = ?
            """;
    private static final String DELETE_QUERY = """
            DELETE FROM reviews
            WHERE review_id = ?
            """;
    private static final String GET_BY_COUNT_QUERY = """
                        SELECT review_id, content, is_positive, user_id, film_id, useful
                        FROM reviews
                        ORDER BY useful DESC, review_id
                        LIMIT ?
                        """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT review_id, content, is_positive, user_id, film_id, useful
            FROM reviews
            WHERE review_id = ?
            """;
    private static final String GET_BY_FILM_QUERY = """
            SELECT review_id, content, is_positive, user_id, film_id, useful
            FROM reviews
            WHERE film_id = ?
            ORDER BY useful DESC
            LIMIT ?
            """;
    private static final String ADD_LIKE_QUERY = """
            INSERT INTO reviews_reaction(review_id, user_id, type_reaction)
            VALUES (?, ?, 1)
            """;
    private static final String UPDATE_USEFUL_PLUS_QUERY = """
            UPDATE reviews
            SET useful = useful + 1
            WHERE review_id = ?
            """;
    private static final String UPDATE_USEFUL_MINUS_QUERY = """
            UPDATE reviews
            SET useful = useful - 1
            WHERE review_id = ?
            """;
    private static final String ADD_DISLIKE_QUERY = """
            INSERT INTO reviews_reaction(review_id, user_id, type_reaction)
            VALUES (?, ?, 2)
            """;
    private static final String REMOVE_LIKE_QUERY = """
            DELETE FROM reviews_reaction
            WHERE review_id = ?
            AND user_id = ?
            AND type_reaction = 1
            """;
    private static final String REMOVE_DISLIKE_QUERY = """
            DELETE FROM reviews_reaction
            WHERE review_id = ?
            AND user_id = ?
            AND type_reaction = 2
            """;
    private static final String COUNT_LIKE_QUERY = """
            SELECT COUNT(*) AS COUNT_LIKES
            FROM reviews_reaction
            WHERE review_id = ?
            AND user_id = ?
            AND type_reaction = 1
            """;
    private static final String COUNT_DISLIKE_QUERY = """
            SELECT COUNT(*) AS COUNT_LIKES
            FROM reviews_reaction
            WHERE review_id = ?
            AND user_id = ?
            AND type_reaction = 2
            """;

    protected ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Review create(Review review) {
        review.setUseful(0);
        int id = insert(
                INSERT_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful()
        );
        review.setReviewId(id);
        return review;
    }

    @Override
    public Review update(Review review) {
        update(
                UPDATE_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        //так как useful мы не апдейтим напрямую, то достаем актуальный из базы
        Optional<Review> optional = findById(review.getReviewId());
        return optional.orElse(review);
    }

    @Override
    public void delete(Integer reviewId) {
        deleteByParam(DELETE_QUERY, reviewId);
    }

    @Override
    public List<Review> getByCount(int count) {
        return findMany(GET_BY_COUNT_QUERY, count);
    }

    @Override
    public Optional<Review> findById(Integer reviewId) {
        return findOne(FIND_BY_ID_QUERY, reviewId);
    }

    @Override
    public List<Review> getByFilmAndCount(Integer filmId, int count) {
        return findMany(GET_BY_FILM_QUERY, filmId, count);
    }

    @Override
    @Transactional
    public void addLike(Integer reviewId, Integer userId) {
        Integer like = count(COUNT_LIKE_QUERY, reviewId, userId);
        if (like == 0) { //повторно не ставим лайк
            insertRelation(ADD_LIKE_QUERY, reviewId, userId);
            update(UPDATE_USEFUL_PLUS_QUERY, reviewId);
        }
        Integer dislike = count(COUNT_DISLIKE_QUERY, reviewId, userId);
        if (dislike != 0) {  //если был дислайк - снимаем его и выправляем рейтинг
            deleteByParam(REMOVE_DISLIKE_QUERY, reviewId, userId);
            update(UPDATE_USEFUL_PLUS_QUERY, reviewId);
        }
    }

    @Override
    @Transactional
    public void addDisLike(Integer reviewId, Integer userId) {
        Integer dislike = count(COUNT_DISLIKE_QUERY, reviewId, userId);
        if (dislike == 0) { //повторно не ставим дизлайк
            insertRelation(ADD_DISLIKE_QUERY, reviewId, userId);
            update(UPDATE_USEFUL_MINUS_QUERY, reviewId);
        }
        Integer like = count(COUNT_LIKE_QUERY, reviewId, userId);
        if (like != 0) { //если был лайк - снимаем его и выправляем рейтинг
            deleteByParam(REMOVE_LIKE_QUERY, reviewId, userId);
            update(UPDATE_USEFUL_MINUS_QUERY, reviewId);
        }
    }

    @Override
    @Transactional
    public void removeLike(Integer reviewId, Integer userId) {
        Integer like = count(COUNT_LIKE_QUERY, reviewId, userId);
        if (like != 0) { //если был лайк - снимаем его и выправляем рейтинг
            deleteByParam(REMOVE_LIKE_QUERY, reviewId, userId);
            update(UPDATE_USEFUL_MINUS_QUERY, reviewId);
        }
    }

    @Override
    @Transactional
    public void removeDisLike(Integer reviewId, Integer userId) {
        Integer dislike = count(COUNT_DISLIKE_QUERY, reviewId, userId);
        if (dislike != 0) {  //если был дислайк - снимаем его и выправляем рейтинг
            deleteByParam(REMOVE_DISLIKE_QUERY, reviewId, userId);
            update(UPDATE_USEFUL_PLUS_QUERY, reviewId);
        }
    }
}
