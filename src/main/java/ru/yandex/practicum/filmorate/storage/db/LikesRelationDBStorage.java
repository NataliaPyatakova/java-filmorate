package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.LikesRelation;
import ru.yandex.practicum.filmorate.storage.LikesRelationStorage;

import java.util.List;

@Component
public class LikesRelationDBStorage extends BaseStorage<LikesRelation> implements LikesRelationStorage {

    private static final String INSERT_LIKE_QUERY = """
            INSERT INTO LIKES_RELATION
            (FILM_ID,USER_ID)
            VALUES (?, ?)
            """;
    private static final String DELETE_LIKE_QUERY = "DELETE FROM LIKES_RELATION WHERE FILM_ID = ? AND USER_ID = ?";
    private static final String COUNT_LIKE_QUERY = "SELECT COUNT(DISTINCT USER_ID) AS COUNT_LIKES FROM LIKES_RELATION WHERE FILM_ID = ?";
    private static final String COUNT_LIKE_USER_QUERY = """
            SELECT COUNT(DISTINCT USER_ID) AS COUNT_LIKES
            FROM LIKES_RELATION
            WHERE FILM_ID = ?
            AND USER_ID = ?
            """;
    private static final String GET_ALL_LIKES_QUERY = "SELECT FILM_ID, USER_ID FROM LIKES_RELATION";

    public LikesRelationDBStorage(JdbcTemplate jdbc, RowMapper<LikesRelation> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addLike(Film film, Integer userId) {
        Integer like = count(COUNT_LIKE_USER_QUERY, film.getId(), userId);
        if (like == 0) {
            insertRelation(INSERT_LIKE_QUERY, film.getId(), userId);
        }
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        deleteByParam(DELETE_LIKE_QUERY, film.getId(), userId);
    }

    @Override
    public Integer countLikesByFilmId(Integer id) {
        return count(COUNT_LIKE_QUERY, id);
    }

    @Override
    public List<LikesRelation> get() {
        return findMany(GET_ALL_LIKES_QUERY);
    }
}
