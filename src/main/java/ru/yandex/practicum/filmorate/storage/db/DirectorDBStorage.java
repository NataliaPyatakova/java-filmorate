package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.DirectorsRelation;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Repository
public class DirectorDBStorage extends BaseStorage<Director> implements DirectorStorage {

    private static final String FIND_ALL_QUERY = """
            SELECT DIRECTOR_ID, DIRECTOR_NAME
            FROM DIRECTORS
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT DIRECTOR_ID, DIRECTOR_NAME
            FROM DIRECTORS
            WHERE DIRECTOR_ID = ?
            """;
    private static final String INSERT_DIRECTOR_QUERY = """
            INSERT INTO DIRECTORS(DIRECTOR_NAME)
            VALUES (?)
            """;
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE DIRECTORS SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
    private static final String DELETE_DIRECTOR_QUERY = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
    private static final String FIND_FILM_DIRECTORS_ID_QUERY = """
            SELECT DISTINCT D.DIRECTOR_ID, D.DIRECTOR_NAME
            FROM DIRECTORS_RELATION DR
            JOIN DIRECTORS D ON D.DIRECTOR_ID = DR.DIRECTOR_ID
            WHERE DR.FILM_ID = ?
            """;
    private static final String FIND_ALL_FILM_DIRECTORS_QUERY = """
            SELECT DISTINCT DR.FILM_ID, D.DIRECTOR_ID, D.DIRECTOR_NAME
            FROM DIRECTORS_RELATION DR
            JOIN DIRECTORS D ON D.DIRECTOR_ID = DR.DIRECTOR_ID
            """;

    private final RowMapper<DirectorsRelation> directorsRelationRowMapper;

    public DirectorDBStorage(JdbcTemplate jdbc, RowMapper<Director> mapper,
                             RowMapper<DirectorsRelation> directorsRelationRowMapper) {
        super(jdbc, mapper);
        this.directorsRelationRowMapper = directorsRelationRowMapper;
    }

    @Override
    public List<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Director> findDirectorById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Director create(Director director) {
        int id = insert(
                INSERT_DIRECTOR_QUERY,
                director.getName()
        );
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director director) {
        update(
                UPDATE_DIRECTOR_QUERY,
                director.getName(),
                director.getId()
        );
        return director;
    }

    @Override
    public void delete(Integer directorId) {
        deleteByParam(DELETE_DIRECTOR_QUERY, directorId);
    }

    @Override
    public Set<Director> findDirectorByFilmId(Integer filmId) {
        return new TreeSet<>(findMany(FIND_FILM_DIRECTORS_ID_QUERY, filmId));
    }

    @Override
    public List<DirectorsRelation> findDirectorsRelations() {
        //return List.of();
        return jdbc.query(FIND_ALL_FILM_DIRECTORS_QUERY, directorsRelationRowMapper);
    }
}
