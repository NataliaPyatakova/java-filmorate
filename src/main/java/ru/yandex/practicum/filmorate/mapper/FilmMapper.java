package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.TreeSet;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(MpaRatingMapper.mapToMpaRatingDto(film.getMpa()));
        dto.setGenres(film.getGenres().stream().map(GenreMapper::mapToGenreDto).collect(Collectors.toCollection(TreeSet::new)));  //для правильной сортировки
        return dto;
    }

    public static Film mapToFilm(NewFilmDto newFilmDto) {
        Film film = new Film();
        film.setName(newFilmDto.getName());
        film.setDescription(newFilmDto.getDescription());
        film.setReleaseDate(newFilmDto.getReleaseDate());
        film.setDuration(newFilmDto.getDuration());
        film.setMpa(MpaRatingMapper.mapToMpaRating(newFilmDto.getMpa()));
        film.setGenres(newFilmDto.getGenres().stream().map(GenreMapper::mapToGenre).collect(Collectors.toSet()));
        return film;
    }

    public static Film updateFilmFields(Film oldFilm, UpdateFilmDto updateFilmDto) {
        Film newFilm = new Film();
        newFilm.setId(oldFilm.getId());
        if (updateFilmDto.hasName()) {
            newFilm.setName(updateFilmDto.getName());
        } else {
            newFilm.setName(oldFilm.getName());
        }
        if (updateFilmDto.hasDescription()) {
            newFilm.setDescription(updateFilmDto.getDescription());
        } else {
            newFilm.setDescription(oldFilm.getDescription());
        }
        if (updateFilmDto.hasReleaseDate()) {
            newFilm.setReleaseDate(updateFilmDto.getReleaseDate());
        } else {
            newFilm.setReleaseDate(oldFilm.getReleaseDate());
        }
        if (updateFilmDto.hasDuration()) {
            newFilm.setDuration(updateFilmDto.getDuration());
        } else {
            newFilm.setDuration(oldFilm.getDuration());
        }
        if (updateFilmDto.hasMpa()) {
            newFilm.setMpa(MpaRatingMapper.mapToMpaRating(updateFilmDto.getMpa()));
        } else {
            newFilm.setMpa(oldFilm.getMpa());
        }
        if (updateFilmDto.hasGenres()) {
            newFilm.setGenres(updateFilmDto.getGenres().stream().map(GenreMapper::mapToGenre).collect(Collectors.toSet()));
        } else {
            newFilm.setGenres(oldFilm.getGenres());
        }
        return newFilm;
    }
}
