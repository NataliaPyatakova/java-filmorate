package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class FilmorateApplicationTests {

    private UserController userController;
    private FilmController filmController;
    private static User user;
    private static User user1;
    private static Film film;
    private static Film film1;

    @BeforeAll
    static void beforeAll() {
        user = new User();
        user.setLogin("user");
        user.setName("user");
        user.setEmail("email@ya.ru");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user1 = new User();
        user1.setLogin("user1");
        user1.setName("user1");
        user1.setEmail("email@ya.ru");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1990, 1, 1));
        film.setDuration(200);
        film1 = new Film();
        film1.setName("film1");
        film1.setDescription("description1");
        film1.setReleaseDate(LocalDate.of(1990, 1, 1));
        film1.setDuration(100);
    }

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        filmController = new FilmController();
    }

    @Test
    @DisplayName("Проверка GET на пустом списке пользователей")
    void testFindAll_NoUsers() {
        List<User> users = userController.findAll();
        Assertions.assertEquals(0, users.size());
    }

    @Test
    @DisplayName("Проверка GET и POST на одном пользователе")
    void testFindAll_1User() {
        userController.save(user);
        List<User> users = userController.findAll();
        User savedUser = userController.findAll().getFirst();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, users.size()),
                () -> Assertions.assertEquals(user.getName(), savedUser.getName()),
                () -> Assertions.assertEquals(user.getLogin(), savedUser.getLogin()),
                () -> Assertions.assertEquals(user.getEmail(), savedUser.getEmail()),
                () -> Assertions.assertEquals(user.getBirthday(), savedUser.getBirthday())
        );
    }

    @Test
    @DisplayName("Проверка GET и POST на двух пользователях")
    void testFindAll_2Users() {
        userController.save(user);
        userController.save(user1);
        List<User> users = userController.findAll();
        User savedUser = userController.findAll().getFirst();
        User savedUser1 = userController.findAll().get(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, users.size()),
                () -> Assertions.assertEquals(user.getName(), savedUser.getName()),
                () -> Assertions.assertEquals(user.getLogin(), savedUser.getLogin()),
                () -> Assertions.assertEquals(user.getEmail(), savedUser.getEmail()),
                () -> Assertions.assertEquals(user.getBirthday(), savedUser.getBirthday()),
                () -> Assertions.assertEquals(user1.getName(), savedUser1.getName()),
                () -> Assertions.assertEquals(user1.getLogin(), savedUser1.getLogin()),
                () -> Assertions.assertEquals(user1.getEmail(), savedUser1.getEmail()),
                () -> Assertions.assertEquals(user1.getBirthday(), savedUser1.getBirthday())
        );
    }

    @Test
    @DisplayName("Проверка PUT на одном пользователе")
    void testUpdateUser() {
        userController.save(user);
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setName("newName");
        newUser.setLogin("newLogin");
        newUser.setEmail("newEmail@ya.ru");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));
        User updatedUser = userController.update(newUser);
        Assertions.assertAll(
                () -> Assertions.assertEquals(user.getName(), updatedUser.getName()),
                () -> Assertions.assertEquals(user.getLogin(), updatedUser.getLogin()),
                () -> Assertions.assertEquals(user.getEmail(), updatedUser.getEmail()),
                () -> Assertions.assertEquals(user.getBirthday(), updatedUser.getBirthday())
        );
    }

    @Test
    @DisplayName("Проверка PUT на пользователе c отсутствующим id")
    void testUpdateUserWithNoId() {
        userController.save(user);
        user1.setId(null);
        try {
            userController.update(user1);
        } catch (ValidationException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT на пользователе c неверным id")
    void testUpdateUserWithWrongId() {
        userController.save(user);
        user1.setId(999999);
        try {
            userController.update(user1);
        } catch (ValidationException e) {
            Assertions.assertEquals("Пользователь с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка POST на пользователе c пустым Name")
    void testSaveUserWithEmptyName() {
        user.setName("");
        User savedUser = userController.save(user);
        Assertions.assertEquals(savedUser.getLogin(), savedUser.getName());
    }

    @Test
    @DisplayName("Проверка GET на пустом списке фильмов")
    void testFindAll_NoFilms() {
        List<Film> films = filmController.findAll();
        Assertions.assertEquals(0, films.size());
    }

    @Test
    @DisplayName("Проверка GET и POST на одном фильме")
    void testFindAll_1Film() {
        filmController.save(film);
        List<Film> films = filmController.findAll();
        Film savedFilm = filmController.findAll().getFirst();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, films.size()),
                () -> Assertions.assertEquals(film.getName(), savedFilm.getName()),
                () -> Assertions.assertEquals(film.getDescription(), savedFilm.getDescription()),
                () -> Assertions.assertEquals(film.getReleaseDate(), savedFilm.getReleaseDate()),
                () -> Assertions.assertEquals(film.getDuration(), savedFilm.getDuration())
        );
    }

    @Test
    @DisplayName("Проверка GET и POST на двух фильмах")
    void testFindAll_2Films() {
        filmController.save(film);
        filmController.save(film1);
        List<Film> films = filmController.findAll();
        Film savedFilm = filmController.findAll().getFirst();
        Film savedFilm1 = filmController.findAll().get(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, films.size()),
                () -> Assertions.assertEquals(film.getName(), savedFilm.getName()),
                () -> Assertions.assertEquals(film.getDescription(), savedFilm.getDescription()),
                () -> Assertions.assertEquals(film.getReleaseDate(), savedFilm.getReleaseDate()),
                () -> Assertions.assertEquals(film.getDuration(), savedFilm.getDuration()),
                () -> Assertions.assertEquals(film1.getName(), savedFilm1.getName()),
                () -> Assertions.assertEquals(film1.getDescription(), savedFilm1.getDescription()),
                () -> Assertions.assertEquals(film1.getReleaseDate(), savedFilm1.getReleaseDate()),
                () -> Assertions.assertEquals(film1.getDuration(), savedFilm1.getDuration())
        );
    }

    @Test
    @DisplayName("Проверка PUT на одном фильме")
    void testUpdateFilm() {
        filmController.save(film);
        Film newFilm = new Film();
        newFilm.setId(film.getId());
        newFilm.setName("newName");
        newFilm.setDescription("newLogin");
        newFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm.setDuration(500);
        Film updatedFilm = filmController.update(newFilm);
        Assertions.assertAll(
                () -> Assertions.assertEquals(film.getName(), updatedFilm.getName()),
                () -> Assertions.assertEquals(film.getDescription(), updatedFilm.getDescription()),
                () -> Assertions.assertEquals(film.getReleaseDate(), updatedFilm.getReleaseDate()),
                () -> Assertions.assertEquals(film.getDuration(), updatedFilm.getDuration())
        );
    }

    @Test
    @DisplayName("Проверка PUT на фильме c отсутствующим id")
    void testUpdateFilmWithNoId() {
        filmController.save(film);
        film1.setId(null);
        try {
            filmController.update(film1);
        } catch (ValidationException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT на фильме c неверным id")
    void testUpdateFilmWithWrongId() {
        filmController.save(film);
        film1.setId(999999);
        try {
            filmController.update(film1);
        } catch (ValidationException e) {
            Assertions.assertEquals("Фильм с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT на фильме c неверной датой")
    void testUpdateFilmWithWrongDate() {
        filmController.save(film);
        Film newFilm = new Film();
        newFilm.setId(film.getId());
        newFilm.setReleaseDate(LocalDate.of(1890, 1, 1));
        try {
            filmController.update(newFilm);
        } catch (ValidationException e) {
            Assertions.assertEquals("Дата релиза должна быть не раньше 1895-12-28", e.getMessage());
        }
    }

    //остальная валидация переложена на аннотации
}
