package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT FILM_ID, " +
                                                 "FILM_NAME, " +
                                                 "DESCRIPTION, " +
                                                 "RELEASE_DATE, " +
                                                 "DURATION, " +
                                                 "RATING_ID " +
                                                 "FROM FILMS";
    private static final String FIND_BY_ID_QUERY = "SELECT FILM_ID, " +
                                                   "FILM_NAME, " +
                                                   "DESCRIPTION, " +
                                                   "RELEASE_DATE, " +
                                                   "DURATION, " +
                                                   "RATING_ID " +
                                                   "FROM FILMS " +
                                                   "WHERE FILM_ID = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO FILMS" +
                                                    "(FILM_NAME,DESCRIPTION,RELEASE_DATE,DURATION,RATING_ID) " +
                                                    "VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO GENRES_RELATION" +
                                                     "(FILM_ID,GENRE_ID) " +
                                                     "VALUES (?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE FILMS " +
                                                    "SET FILM_NAME = ?, " +
                                                    "DESCRIPTION = ?, " +
                                                    "RELEASE_DATE = ?, " +
                                                    "DURATION = ?, " +
                                                    "RATING_ID = ? " +
                                                    "WHERE FILM_ID = ?";
    private static final String DELETE_ALL_QUERY = "DELETE FROM FILMS";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO LIKES_RELATION" +
                                                    "(FILM_ID,USER_ID) " +
                                                    "VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM LIKES_RELATION WHERE FILM_ID = ? AND USER_ID = ?";
    private static final String COUNT_LIKE_QUERY = "SELECT COUNT(DISTINCT USER_ID) AS COUNT_LIKES FROM LIKES_RELATION WHERE FILM_ID = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> findById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Film save(Film film) {
        Integer id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        film.getGenres().forEach(genre ->  insert(INSERT_GENRE_QUERY, film.getId(), genre.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public void addLike(Film film, Integer userId) {
        insert(INSERT_LIKE_QUERY, film.getId(), userId);
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        deleteByParam(DELETE_LIKE_QUERY, film.getId(), userId);
    }

    @Override
    public void deleteAll() {
        delete(DELETE_ALL_QUERY);
    }

    @Override
    public Integer countLikesByFilmId(Integer id) {
        return count(COUNT_LIKE_QUERY, id);
    }
}
