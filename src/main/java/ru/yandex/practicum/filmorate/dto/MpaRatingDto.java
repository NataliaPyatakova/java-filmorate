package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MpaRatingDto {

    @NotNull
    private Integer id;
    @NotBlank
    private String name;
}
