package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Service
@Slf4j
public class MpaRatingService {

    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingService(@Qualifier("MpaRatingDBStorage") MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public List<MpaRatingDto> findAll() {
        return mpaRatingStorage.findAll().stream().map(MpaRatingMapper::mapToMpaRatingDto).toList();
    }

    public MpaRatingDto findById(Integer id) {
        return MpaRatingMapper.mapToMpaRatingDto(findMpaRatingById(id));
    }

    public MpaRating findMpaRatingById(Integer id) {
        return mpaRatingStorage.findById(id).orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден"));
    }
}
