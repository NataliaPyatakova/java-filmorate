package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.NewDirectorDto;
import ru.yandex.practicum.filmorate.dto.UpdateDirectorDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.DirectorsRelation;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<DirectorDto> findAll() {
        return directorStorage.findAll().stream().map(DirectorMapper::mapToDirectorDto).toList();
    }

    public DirectorDto findDirectorById(Integer id) {
        Director foundedDirector = directorStorage.findDirectorById(id)
                .orElseThrow(() -> new NotFoundException("Режиссер с id = " + id + " не найден"));
        return DirectorMapper.mapToDirectorDto(foundedDirector);
    }

    public DirectorDto create(NewDirectorDto newDirectorDto) {
        log.info("Сохраняем директора {}", newDirectorDto);
        Director newDirector = directorStorage.create(DirectorMapper.mapToDirector(newDirectorDto));
        return DirectorMapper.mapToDirectorDto(newDirector);
    }

    public DirectorDto update(UpdateDirectorDto updateDirectorDto) {
        log.info("Обновляем директора {}", updateDirectorDto);
        findDirectorById(updateDirectorDto.getId());
        Director newDirector = directorStorage.update(DirectorMapper.mapToDirector(updateDirectorDto));
        return DirectorMapper.mapToDirectorDto(newDirector);
    }

    public void delete(Integer directorId) {
        findDirectorById(directorId);
        directorStorage.delete(directorId);
    }

    public Set<Director> findDirectorByFilmId(Integer filmId) {
        return directorStorage.findDirectorByFilmId(filmId);
    }

    public List<DirectorsRelation> findDirectorsRelations() {
        return directorStorage.findDirectorsRelations();
    }
}
