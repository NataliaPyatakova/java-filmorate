package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryMpaRatingStorage implements MpaRatingStorage {

    private final Map<Integer, MpaRating> mpaRatings = new HashMap<>();

    public InMemoryMpaRatingStorage() {
        mpaRatings.clear();
        mpaRatings.put(1, new MpaRating().setId(1).setName("G"));
        mpaRatings.put(2, new MpaRating().setId(2).setName("PG"));
        mpaRatings.put(3, new MpaRating().setId(3).setName("PG-13"));
        mpaRatings.put(4, new MpaRating().setId(4).setName("R"));
        mpaRatings.put(5, new MpaRating().setId(5).setName("NC-17"));
    }

    @Override
    public List<MpaRating> findAll() {
        return mpaRatings.values().stream().toList();
    }

    @Override
    public Optional<MpaRating> findById(Integer id) {
        return mpaRatings.values().stream()
                .filter(rating -> rating.getId().equals(id))
                .findFirst();
    }
}
