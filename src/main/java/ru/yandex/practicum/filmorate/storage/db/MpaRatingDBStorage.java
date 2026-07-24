package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("MpaRatingDBStorage")
public class MpaRatingDBStorage extends BaseStorage<MpaRating> implements MpaRatingStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM RATINGS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM RATINGS WHERE RATING_ID = ?";

    public MpaRatingDBStorage(JdbcTemplate jdbc, RowMapper<MpaRating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<MpaRating> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<MpaRating> findById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}
