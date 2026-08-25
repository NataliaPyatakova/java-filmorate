package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.LikesRelation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikesRelationMapper implements RowMapper<LikesRelation> {

    @Override
    public LikesRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
        LikesRelation likesRelation = new LikesRelation();
        likesRelation.setFilmId(rs.getInt("film_id"));
        likesRelation.setUserId(rs.getInt("user_id"));
        return likesRelation;
    }
}
