package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
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

    private static final String FIND_BY_IDS_QUERY = """
            SELECT f.FILM_ID,
                   f.FILM_NAME,
                   f.DESCRIPTION,
                   f.RELEASE_DATE,
                   f.DURATION,
                   f.RATING_ID
            FROM FILMS f
            WHERE f.FILM_ID IN (%s)
            """;

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
        film.getGenres().forEach(genre ->  insertRelation(INSERT_GENRE_QUERY, film.getId(), genre.getId()));
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
    public void deleteAll() {
        delete(DELETE_ALL_QUERY);
    }

    @Override
    public List<Film> getByIds(Set<Integer> filmIds) {
        return findMany(FIND_BY_IDS_QUERY.formatted(placeholder(filmIds.size())), filmIds.toArray());
    }
}
