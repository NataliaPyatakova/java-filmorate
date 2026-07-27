package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresRelation;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.*;

@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "SELECT GENRE_ID, GENRE_NAME FROM GENRES";
    private static final String FIND_BY_ID_QUERY = "SELECT GENRE_ID, GENRE_NAME FROM GENRES WHERE GENRE_ID = ?";
    private static final String FIND_FILM_GENRES_ID_QUERY = "SELECT DISTINCT G.GENRE_ID, G.GENRE_NAME " +
                                                            "FROM GENRES_RELATION GR " +
                                                            "JOIN GENRES G ON G.GENRE_ID = GR.GENRE_ID " +
                                                            "WHERE GR.FILM_ID = ?";
    private static final String FIND_ALL_FILM_GENRES_QUERY = "SELECT DISTINCT GR.FILM_ID, G.GENRE_ID, G.GENRE_NAME " +
                                                             "FROM GENRES_RELATION GR " +
                                                             "JOIN GENRES G ON G.GENRE_ID = GR.GENRE_ID ";
    private final RowMapper<GenresRelation> genresRelationRowMapper;

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper, RowMapper<GenresRelation> genresRelationRowMapper) {
        super(jdbc, mapper);
        this.genresRelationRowMapper = genresRelationRowMapper;
    }

    @Override
    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Set<Genre> findByFilmId(Integer id) {
        return new TreeSet<>(findMany(FIND_FILM_GENRES_ID_QUERY, id));
    }

    @Override
    public List<GenresRelation> findFilmGenreRelations() {
        return jdbc.query(FIND_ALL_FILM_GENRES_QUERY, genresRelationRowMapper);
    }
}
