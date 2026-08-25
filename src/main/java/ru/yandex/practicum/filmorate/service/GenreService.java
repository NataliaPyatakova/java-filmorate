package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresRelation;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<GenreDto> findAll() {
        return genreStorage.findAll()
                .stream()
                .sorted(Comparator.comparing(Genre::getId))
                .map(GenreMapper::mapToGenreDto).toList();
    }

    public GenreDto findById(Integer id) {
        return GenreMapper.mapToGenreDto(findGenreById(id));
    }

    public Set<Genre> findByFilmId(Integer id) {
        return genreStorage.findByFilmId(id);
    }

    public List<GenresRelation> findFilmGenreRelations() {
        return genreStorage.findFilmGenreRelations();
    }

    private Genre findGenreById(Integer id) {
        return genreStorage.findById(id).orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден"));
    }
}
