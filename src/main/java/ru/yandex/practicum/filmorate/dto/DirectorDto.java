package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DirectorDto implements Comparable<DirectorDto> {

    private Integer id;
    private String name;

    @Override
    public int compareTo(DirectorDto o) {
        return this.id.compareTo(o.id);
    }
}
