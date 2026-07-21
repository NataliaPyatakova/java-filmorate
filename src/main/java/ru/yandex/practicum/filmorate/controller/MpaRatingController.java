package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    @GetMapping
    public List<MpaRating> findAll() {
        return mpaRatingService.findAll();
    }

    @GetMapping("/{id}")
    public MpaRating findById(@PathVariable("id") Integer id) {
        return mpaRatingService.findById(id);
    }
}
