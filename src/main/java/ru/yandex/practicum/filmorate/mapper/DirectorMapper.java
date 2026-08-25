package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.NewDirectorDto;
import ru.yandex.practicum.filmorate.dto.UpdateDirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectorMapper {

    public static DirectorDto mapToDirectorDto(Director director) {
        DirectorDto directorDto = new DirectorDto();
        directorDto.setId(director.getId());
        directorDto.setName(director.getName());
        return directorDto;
    }

    public static Director mapToDirector(DirectorDto directorDto) {
        Director director = new Director();
        director.setId(directorDto.getId());
        director.setName(directorDto.getName());
        return director;
    }

    public static Director mapToDirector(NewDirectorDto newDirectorDto) {
        Director director = new Director();
        director.setName(newDirectorDto.getName());
        return director;
    }

    public static Director mapToDirector(UpdateDirectorDto updateDirectorDto) {
        Director director = new Director();
        director.setId(updateDirectorDto.getId());
        director.setName(updateDirectorDto.getName());
        return director;
    }
}
