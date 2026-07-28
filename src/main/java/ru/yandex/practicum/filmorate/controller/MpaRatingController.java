package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
@Validated
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    @GetMapping
    public List<MpaRatingDto> findAll() {
        return mpaRatingService.findAll();
    }

    @GetMapping("/{id}")
    public MpaRatingDto findById(@PathVariable("id") @NotNull Integer id) {
        return mpaRatingService.findById(id);
    }
}
