package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<GenreDto> findAll() {
        return genreStorage.findAll().stream().map(GenreMapper::mapToGenreDto).toList();
    }

    public Genre save(Genre genre) {
        log.info("Saving genre {}", genre);
        return genreStorage.save(genre);
    }

    public Genre update(Genre newGenre) {
        log.info("Updating newGenre {}", newGenre);
        Genre oldGenre = findGenreById(newGenre.getId());
        return genreStorage.update(oldGenre, newGenre);
    }

    public GenreDto findById(Integer id) {
        return GenreMapper.mapToGenreDto(findGenreById(id));
    }

    public Genre findGenreById(Integer id) {
        return genreStorage.findById(id).orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден"));
    }
}
