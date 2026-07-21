package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre save(Genre genre) {
        log.info("Saving genre {}", genre);
        return genreStorage.save(genre);
    }

    public Genre update(Genre newGenre) {
        log.info("Updating newGenre {}", newGenre);
        Genre oldGenre = findById(newGenre.getId());
        return genreStorage.update(oldGenre, newGenre);
    }

    public Genre findById(Integer id) {
        return genreStorage.findById(id).orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден"));
    }
}
