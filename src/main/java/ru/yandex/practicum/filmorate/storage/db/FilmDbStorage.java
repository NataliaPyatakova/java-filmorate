package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.enumeration.FilmByField;
import ru.yandex.practicum.filmorate.enumeration.FilmSortField;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = """
            SELECT FILM_ID,
            FILM_NAME,
            DESCRIPTION,
            RELEASE_DATE,
            DURATION,
            RATING_ID
            FROM FILMS
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT FILM_ID,
            FILM_NAME,
            DESCRIPTION,
            RELEASE_DATE,
            DURATION,
            RATING_ID
            FROM FILMS
            WHERE FILM_ID = ?
            """;
    private static final String INSERT_FILM_QUERY = """
            INSERT INTO FILMS
            (FILM_NAME,DESCRIPTION,RELEASE_DATE,DURATION,RATING_ID)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String INSERT_GENRE_QUERY = """
            INSERT INTO GENRES_RELATION
            (FILM_ID,GENRE_ID)
            VALUES (?, ?)
            """;
    private static final String INSERT_DIRECTOR_QUERY = """
            INSERT INTO DIRECTORS_RELATION
            (FILM_ID,DIRECTOR_ID)
            VALUES (?, ?)
            """;
    private static final String DELETE_GENRE_QUERY = "DELETE FROM GENRES_RELATION WHERE FILM_ID = ?";
    private static final String DELETE_DIRECTOR_QUERY = "DELETE FROM DIRECTORS_RELATION WHERE FILM_ID = ?";
    private static final String UPDATE_FILM_QUERY = """
            UPDATE FILMS
            SET FILM_NAME = ?,
            DESCRIPTION = ?,
            RELEASE_DATE = ?,
            DURATION = ?,
            RATING_ID = ?
            WHERE FILM_ID = ?
            """;
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
    private static final String DELETE_FILM_QUERY = "DELETE FROM FILMS WHERE FILM_ID = ?";
    private static final String GET_POPULAR_QUERY = """
            SELECT f.FILM_ID,
            f.FILM_NAME,
            f.DESCRIPTION,
            f.RELEASE_DATE,
            f.DURATION,
            f.RATING_ID,
            COUNT(DISTINCT lr.USER_ID) AS likes
            FROM FILMS f
            LEFT JOIN LIKES_RELATION     lr ON lr.FILM_ID = f.FILM_ID
            LEFT JOIN GENRES_RELATION as gr ON gr.FILM_ID = f.FILM_ID
            WHERE %s
            GROUP BY f.FILM_ID
            ORDER BY likes DESC, f.FILM_ID
            LIMIT ?
            """;
    private static final String GET_COMMON_FILMS_QUERY = """
            SELECT f.FILM_ID,
            f.FILM_NAME,
            f.DESCRIPTION,
            f.RELEASE_DATE,
            f.DURATION,
            f.RATING_ID,
            COUNT(fl_all.USER_ID) AS likes
            FROM FILMS f
            JOIN LIKES_RELATION lr1 ON lr1.FILM_ID = f.FILM_ID AND lr1.USER_ID = ?
            JOIN LIKES_RELATION lr2 ON lr2.FILM_ID = f.FILM_ID AND lr2.USER_ID = ?
            JOIN LIKES_RELATION fl_all ON fl_all.FILM_ID = f.FILM_ID
            GROUP BY f.FILM_ID
            ORDER BY likes DESC;
            """;
    private static final String GET_BY_DIRECTOR_QUERY = """
            SELECT f.FILM_ID,
            f.FILM_NAME,
            f.DESCRIPTION,
            f.RELEASE_DATE,
            f.DURATION,
            f.RATING_ID,
            COUNT(DISTINCT lr.USER_ID) AS likes
            FROM FILMS f
            JOIN DIRECTORS_RELATION  dr ON dr.FILM_ID = f.FILM_ID
            LEFT JOIN LIKES_RELATION lr ON lr.FILM_ID = f.FILM_ID
            WHERE dr.DIRECTOR_ID = ?
            GROUP BY f.FILM_ID
            ORDER BY %s
            """;
    private static final String SEARCH_BY = """
            SELECT f.FILM_ID,
                   f.FILM_NAME,
                   f.DESCRIPTION,
                   f.RELEASE_DATE,
                   f.DURATION,
                   f.RATING_ID,
                   COUNT(DISTINCT lr.USER_ID) AS likes
            FROM FILMS F
            LEFT JOIN DIRECTORS_RELATION DR ON DR.FILM_ID = F.FILM_ID
            LEFT JOIN DIRECTORS D ON D.DIRECTOR_ID = DR.DIRECTOR_ID
            LEFT JOIN LIKES_RELATION lr ON lr.FILM_ID = F.FILM_ID
            WHERE %s
            GROUP BY F.FILM_ID
            ORDER BY likes DESC
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
    @Transactional
    public Film save(Film film) {
        Integer id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        film.getGenres().forEach(genre -> insertRelation(INSERT_GENRE_QUERY, film.getId(), genre.getId()));
        film.getDirectors().forEach(director -> insertRelation(INSERT_DIRECTOR_QUERY, film.getId(), director.getId()));
        return film;
    }

    @Override
    @Transactional
    public Film update(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteByParam(DELETE_GENRE_QUERY, film.getId());
        film.getGenres().forEach(genre -> insertRelation(INSERT_GENRE_QUERY, film.getId(), genre.getId()));
        deleteByParam(DELETE_DIRECTOR_QUERY, film.getId());
        film.getDirectors().forEach(director -> insertRelation(INSERT_DIRECTOR_QUERY, film.getId(), director.getId()));
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

    @Override
    public void deleteById(Integer id) {
        deleteByParam(DELETE_FILM_QUERY, id);
    }

    @Override
    public List<Film> getPopular(int count, Integer genreId, Integer year) {
        List<String> condition = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (genreId != null) {
            condition.add("gr.GENRE_ID = ?");
            params.add(genreId);
        }
        if (year != null) {
            condition.add("EXTRACT(YEAR FROM f.RELEASE_DATE) = ?");
            params.add(year);
        }
        params.add(count);
        //"1=1" так как форматирование требует чтобы это не было пустым
        String where = condition.isEmpty() ? "1=1" : String.join(" AND ", condition);
        String finalQuery = GET_POPULAR_QUERY.formatted(where);
        return findMany(finalQuery, params.toArray());
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return findMany(GET_COMMON_FILMS_QUERY, userId, friendId);
    }

    @Override
    public List<Film> getByDirector(Integer directorId, List<FilmSortField> sortBy) {
        String orderBy = sortBy.stream()
                .map(FilmSortField::getSqlField)
                .collect(Collectors.joining(", "));
        String query = GET_BY_DIRECTOR_QUERY.formatted(orderBy);
        return findMany(query, directorId);
    }

    @Override
    public List<Film> search(String query, List<FilmByField> by) {
        String pattern = "%" + query.toLowerCase() + "%";
        String byDirectorAndOrTitle = by.stream()
                .map(FilmByField::getSqlField)
                .collect(Collectors.joining(" OR "));
        String finalQuery = SEARCH_BY.formatted(byDirectorAndOrTitle);
        Object[] params = new Object[by.size()];
        Arrays.fill(params, pattern);
        return findMany(finalQuery, params);
    }
}
