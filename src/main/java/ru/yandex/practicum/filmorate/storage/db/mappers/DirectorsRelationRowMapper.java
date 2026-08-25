package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.DirectorsRelation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorsRelationRowMapper implements RowMapper<DirectorsRelation> {

    @Override
    public DirectorsRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
        DirectorsRelation directorsRelation = new DirectorsRelation();
        directorsRelation.setId(rs.getInt("FILM_ID"));
        directorsRelation.setDirector(new Director().setId(rs.getInt("DIRECTOR_ID")).setName(rs.getString("DIRECTOR_NAME")));
        return directorsRelation;
    }
}
