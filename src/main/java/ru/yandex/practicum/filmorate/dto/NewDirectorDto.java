package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewDirectorDto {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;
}
