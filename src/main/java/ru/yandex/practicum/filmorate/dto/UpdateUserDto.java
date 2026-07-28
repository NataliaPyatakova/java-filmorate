package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class UpdateUserDto {

    @NotNull
    private Integer id;
    @Email
    private String email;
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return ! (login == null || login.isBlank());
    }

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}
