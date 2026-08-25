package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateDirectorDto {

    @NotNull
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
}
