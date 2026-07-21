package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaRatingService {

    private final MpaRatingStorage mpaRatingStorage;

    public List<MpaRating> findAll() {
        return mpaRatingStorage.findAll();
    }

    public MpaRating findById(Integer id) {
        return mpaRatingStorage.findById(id).orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден"));
    }
}
