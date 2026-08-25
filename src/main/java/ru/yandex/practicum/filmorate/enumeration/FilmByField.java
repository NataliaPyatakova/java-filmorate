package ru.yandex.practicum.filmorate.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilmByField {
    title("LOWER(F.FILM_NAME) LIKE ?"),
    director("LOWER(D.DIRECTOR_NAME) LIKE ?");

    private final String sqlField;
}
