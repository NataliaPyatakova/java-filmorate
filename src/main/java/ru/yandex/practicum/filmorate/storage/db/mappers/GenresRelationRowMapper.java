package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresRelation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenresRelationRowMapper implements RowMapper<GenresRelation> {

    @Override
    public GenresRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
        GenresRelation genresRelation = new GenresRelation();
        genresRelation.setId(rs.getInt("FILM_ID"));
        genresRelation.setGenre(new Genre().setId(rs.getInt("GENRE_ID")).setName(rs.getString("GENRE_NAME")));
        return genresRelation;
    }
}
