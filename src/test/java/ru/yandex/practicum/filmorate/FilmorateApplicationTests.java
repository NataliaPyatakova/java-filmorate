package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaRatingService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
class FilmorateApplicationTests {

    private UserController userController;
    private FilmController filmController;
    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private MpaRatingService mpaRatingService;
    private static NewUserDto user;
    private static NewUserDto user1;
    private static NewUserDto user2;
    private static NewUserDto user3;
    private static NewUserDto user4;
    private static UpdateUserDto newUser;
    private static NewFilmDto film;
    private static NewFilmDto film1;
    private static NewFilmDto film2;
    private static NewFilmDto film3;
    private static NewFilmDto film4;
    private static UpdateFilmDto newFilm;


    @BeforeEach
    void beforeEach() {
        userController = new UserController(userService);
        filmController = new FilmController(filmService);
        filmService.deleteAll();
        userService.deleteAll();
        MpaRatingDto mpaRating = mpaRatingService.findAll().getFirst();
        user = new NewUserDto()
                .setLogin("user")
                .setName("user")
                .setEmail("email@ya.ru")
                .setBirthday(LocalDate.of(1990, 1, 1));
        user1 = new NewUserDto()
                .setLogin("user1")
                .setName("user1")
                .setEmail("email1@ya.ru")
                .setBirthday(LocalDate.of(1990, 1, 1));
        user2 = new NewUserDto()
                .setLogin("user2")
                .setName("user2")
                .setEmail("email2@ya.ru")
                .setBirthday(LocalDate.of(1958, 1, 1));
        user3 = new NewUserDto()
                .setLogin("user3")
                .setName("user3")
                .setEmail("email3@ya.ru")
                .setBirthday(LocalDate.of(1958, 1, 1));
        user4 = new NewUserDto()
                .setLogin("user4")
                .setName("user4")
                .setEmail("email4@ya.ru")
                .setBirthday(LocalDate.of(1958, 1, 1));
        newUser = new UpdateUserDto()
                .setLogin("newLogin")
                .setName("newName")
                .setEmail("newEmail@ya.ru")
                .setBirthday(LocalDate.of(2000, 1, 1));
        film = new NewFilmDto()
                .setName("film")
                .setDescription("description")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(200)
                .setMpa(mpaRating);
        film1 = new NewFilmDto()
                .setName("film1")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100)
                .setMpa(mpaRating);
        film2 = new NewFilmDto()
                .setName("film2")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100)
                .setMpa(mpaRating);
        film3 = new NewFilmDto()
                .setName("film3")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100)
                .setMpa(mpaRating);
        film4 = new NewFilmDto()
                .setName("film4")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100)
                .setMpa(mpaRating);
        newFilm = new UpdateFilmDto()
                .setName("newName")
                .setDescription("newLogin")
                .setReleaseDate(LocalDate.of(2000, 1, 1))
                .setDuration(500)
                .setMpa(mpaRating);
    }

    //ТЕСТЫ НА USER
    @Test
    @DisplayName("Проверка GET на пустом списке пользователей")
    void testFindAll_NoUsers() {
        List<UserDto> users = userController.findAll();
        Assertions.assertEquals(0, users.size());
    }

    @Test
    @DisplayName("Проверка GET и POST на одном пользователе")
    void testFindAll_1User() {
        UserDto savedUser = userController.save(user);
        List<UserDto> users = userController.findAll();
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
        List<UserDto> users = userController.findAll();
        UserDto savedUser = userController.findAll().getFirst();
        UserDto savedUser1 = userController.findAll().get(1);
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
        UserDto savedUser = userController.save(user);
        newUser.setId(savedUser.getId());
        UserDto updatedUser = userController.update(newUser);
        Assertions.assertAll(
                () -> Assertions.assertEquals(newUser.getName(), updatedUser.getName()),
                () -> Assertions.assertEquals(newUser.getLogin(), updatedUser.getLogin()),
                () -> Assertions.assertEquals(newUser.getEmail(), updatedUser.getEmail()),
                () -> Assertions.assertEquals(newUser.getBirthday(), updatedUser.getBirthday())
        );
    }

    @Test
    @DisplayName("Проверка PUT на пользователе c отсутствующим id")
    void testUpdateUserWithNoId() {
        userController.save(user);
        newUser.setId(null);
        try {
            userController.update(newUser);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT на пользователе c неверным id")
    void testUpdateUserWithWrongId() {
        userController.save(user);
        newUser.setId(999999);
        try {
            userController.update(newUser);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка POST на пользователе c пустым Name")
    void testSaveUserWithEmptyName() {
        user.setName("");
        UserDto savedUser = userController.save(user);
        Assertions.assertEquals(savedUser.getLogin(), savedUser.getName());
    }

    @Test
    @DisplayName("Проверка GET на отсутствующем пользователе")
    void testGetUserByIDWithNoUser() {
        try {
            userController.findById(null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET на неверном id пользователя")
    void testGetUserByIDWithWrongId() {
        userController.save(user);
        try {
            userController.findById(999999);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET на правильном id пользователя")
    void testGetUserByIdWithRightId() {
        UserDto savedUser = userController.save(user);
        UserDto findUser = userController.findById(savedUser.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(findUser.getName(), savedUser.getName()),
                () -> Assertions.assertEquals(findUser.getLogin(), savedUser.getLogin()),
                () -> Assertions.assertEquals(findUser.getEmail(), savedUser.getEmail()),
                () -> Assertions.assertEquals(findUser.getBirthday(), savedUser.getBirthday())
        );
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей при несущетсвующем пользователе")
    void testAddFriendNoUser() {
        try {
            userController.addFriend(null, null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей при несущетсвующем друге")
    void testAddFriendNoFriend() {
        UserDto savedUser = userController.save(user);
        try {
            userController.addFriend(savedUser.getId(), null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей пользователь дружит с самим собой")
    void testAddFriendFriendYourself() {
        UserDto savedUser = userController.save(user);
        try {
            userController.addFriend(savedUser.getId(), savedUser.getId());
        } catch (ValidationException e) {
            Assertions.assertEquals("Нельзя добавить в друзья самого себя", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей правильный пользователь и друг")
    void testAddFriendRightUserRightFriend() {
        UserDto savedUser = userController.save(user);
        UserDto savedUser1 = userController.save(user1);
        userController.addFriend(savedUser.getId(), savedUser1.getId());
        List<UserDto> friends = userController.findAllFriends(savedUser.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, friends.size()),
                () -> Assertions.assertTrue(friends.contains(savedUser1))
        );
    }

    @Test
    @DisplayName("Проверка DELETE друзей при несущетсвующем пользователе")
    void testDeleteFriendWrongUser() {
        try {
            userController.removeFriend(null, null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE друзей при несущетсвующем друге")
    void testDeleteFriendWrongFriend() {
        UserDto savedUser = userController.save(user);
        try {
            userController.removeFriend(savedUser.getId(), null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE друзей правильный пользователь и друг")
    void testDeleteFriendRightUserRightFriend() {
        UserDto savedUser = userController.save(user);
        UserDto savedUser1 = userController.save(user1);
        userController.removeFriend(savedUser.getId(), savedUser1.getId());
        List<UserDto> friends = userController.findAllFriends(savedUser.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, friends.size())
        );
    }

    @Test
    @DisplayName("Проверка GET всех друзей неправильный пользователь")
    void testFindAllFriendsWrongUser() {
        try {
            userController.findAllFriends(null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET всех друзей правильный пользователь без друзей")
    void testFindAllFriendsNoFriend() {
        UserDto savedUser = userController.save(user);
        List<UserDto> friends = userController.findAllFriends(savedUser.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, friends.size())
        );
    }

    @Test
    @DisplayName("Проверка GET всех друзей правильный пользователь с друзьями")
    void testFindAllFriends() {
        UserDto savedUser = userController.save(user);
        UserDto savedUser1 = userController.save(user1);
        userController.addFriend(savedUser.getId(), savedUser1.getId());
        List<UserDto> friends = userController.findAllFriends(savedUser.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, friends.size()),
                () -> Assertions.assertEquals(friends.getFirst().getId(), savedUser1.getId())
        );
    }

    @Test
    @DisplayName("Проверка GET общих друзей неправильный пользователь")
    void testFindCommonFriendWrongUser() {
        try {
            userController.findCommonFriends(null, null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET общих друзей неправильный друг")
    void testFindCommonFriendWrongFriend() {
        UserDto savedUser = userController.save(user);
        try {
            userController.findCommonFriends(savedUser.getId(), null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET общих друзей без общих друзей")
    void testFindCommonFriendNoCommon() {
        UserDto savedUser = userController.save(user);
        UserDto savedUser1 = userController.save(user1);
        UserDto savedUser2 = userController.save(user2);
        userController.addFriend(savedUser.getId(), savedUser1.getId());
        Set<UserDto> friends = userController.findCommonFriends(savedUser.getId(), savedUser2.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, friends.size())
        );
    }

    @Test
    @DisplayName("Проверка GET общих друзей с общими друзьями")
    void testFindCommonFriend() {
        UserDto savedUser = userController.save(user);
        UserDto savedUser1 = userController.save(user1);
        UserDto savedUser2 = userController.save(user2);
        userController.addFriend(savedUser.getId(), savedUser1.getId());
        userController.addFriend(savedUser2.getId(), savedUser1.getId());
        Set<UserDto> friends = userController.findCommonFriends(savedUser.getId(), savedUser2.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, friends.size()),
                () -> Assertions.assertEquals(friends.iterator().next().getId(), savedUser1.getId())
        );
    }

    //ТЕСТЫ НА FILM
    @Test
    @DisplayName("Проверка GET на пустом списке фильмов")
    void testFindAll_NoFilms() {
        List<FilmDto> films = filmController.findAll();
        Assertions.assertEquals(0, films.size());
    }

    @Test
    @DisplayName("Проверка GET и POST на одном фильме")
    void testFindAll_1Film() {
        filmController.save(film);
        List<FilmDto> films = filmController.findAll();
        FilmDto savedFilm = filmController.findAll().getFirst();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, films.size()),
                () -> Assertions.assertEquals(film.getName(), savedFilm.getName()),
                () -> Assertions.assertEquals(film.getDescription(), savedFilm.getDescription()),
                () -> Assertions.assertEquals(film.getReleaseDate(), savedFilm.getReleaseDate()),
                () -> Assertions.assertEquals(film.getDuration(), savedFilm.getDuration()),
                () -> Assertions.assertEquals(film.getGenres(), savedFilm.getGenres()),
                () -> Assertions.assertEquals(film.getMpa(), savedFilm.getMpa())
        );
    }

    @Test
    @DisplayName("Проверка GET и POST на двух фильмах")
    void testFindAll_2Films() {
        filmController.save(film);
        filmController.save(film1);
        List<FilmDto> films = filmController.findAll();
        FilmDto savedFilm = filmController.findAll().getFirst();
        FilmDto savedFilm1 = filmController.findAll().get(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, films.size()),
                () -> Assertions.assertEquals(film.getName(), savedFilm.getName()),
                () -> Assertions.assertEquals(film.getDescription(), savedFilm.getDescription()),
                () -> Assertions.assertEquals(film.getReleaseDate(), savedFilm.getReleaseDate()),
                () -> Assertions.assertEquals(film.getDuration(), savedFilm.getDuration()),
                () -> Assertions.assertEquals(film.getGenres(), savedFilm.getGenres()),
                () -> Assertions.assertEquals(film.getMpa(), savedFilm.getMpa()),
                () -> Assertions.assertEquals(film1.getName(), savedFilm1.getName()),
                () -> Assertions.assertEquals(film1.getDescription(), savedFilm1.getDescription()),
                () -> Assertions.assertEquals(film1.getReleaseDate(), savedFilm1.getReleaseDate()),
                () -> Assertions.assertEquals(film1.getDuration(), savedFilm1.getDuration()),
                () -> Assertions.assertEquals(film1.getGenres(), savedFilm1.getGenres()),
                () -> Assertions.assertEquals(film1.getMpa(), savedFilm1.getMpa())
        );
    }

    @Test
    @DisplayName("Проверка PUT на одном фильме")
    void testUpdateFilm() {
        FilmDto savedFilm = filmController.save(film);
        newFilm.setId(savedFilm.getId());
        FilmDto updatedFilm = filmController.update(newFilm);
        Assertions.assertAll(
                () -> Assertions.assertEquals(newFilm.getName(), updatedFilm.getName()),
                () -> Assertions.assertEquals(newFilm.getDescription(), updatedFilm.getDescription()),
                () -> Assertions.assertEquals(newFilm.getReleaseDate(), updatedFilm.getReleaseDate()),
                () -> Assertions.assertEquals(newFilm.getDuration(), updatedFilm.getDuration())
        );
    }

    @Test
    @DisplayName("Проверка PUT на фильме c отсутствующим id")
    void testUpdateFilmWithNoId() {
        filmController.save(film);
        newFilm.setId(null);
        try {
            filmController.update(newFilm);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT на фильме c неверным id")
    void testUpdateFilmWithWrongId() {
        filmController.save(film);
        newFilm.setId(999999);
        try {
            filmController.update(newFilm);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT на фильме c неверной датой")
    void testUpdateFilmWithWrongDate() {
        FilmDto savedFilm = filmController.save(film);
        newFilm.setId(savedFilm.getId());
        newFilm.setReleaseDate(LocalDate.of(1890, 1, 1));
        try {
            filmController.update(newFilm);
        } catch (ValidationException e) {
            Assertions.assertEquals("Дата релиза должна быть не раньше 1895-12-28", e.getMessage());
        }
    }

    //остальная валидация переложена на аннотации

    @Test
    @DisplayName("Проверка GET BY ID на отсутстующем фильме")
    void testFindByIdNOFilm() {
        try {
            filmController.findById(null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET BY ID на неверном фильме")
    void testFindByIdWrongFilm() {
        filmController.save(film);
        try {
            filmController.findById(999999);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET BY ID на правильном фильме")
    void testFindById() {
        FilmDto savedFilm = filmController.save(film);
        FilmDto foundFilm = filmController.findById(savedFilm.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(savedFilm.getName(), foundFilm.getName()),
                () -> Assertions.assertEquals(savedFilm.getDescription(), foundFilm.getDescription()),
                () -> Assertions.assertEquals(savedFilm.getReleaseDate(), foundFilm.getReleaseDate()),
                () -> Assertions.assertEquals(savedFilm.getDuration(), foundFilm.getDuration())
        );
    }

    @Test
    @DisplayName("Проверка PUT like на неправильном фильме")
    void testAddLikeNoFilm() {
        UserDto savedUser = userController.save(user);
        try {
            filmController.addLike(null, savedUser.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT like на неправильном пользователе")
    void testAddLikeNoUser() {
        FilmDto savedFilm = filmController.save(film);
        try {
            filmController.addLike(savedFilm.getId(), null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT like на правильном фильме и пользователе")
    void testAddLike() {
        UserDto savedUser = userController.save(user);
        FilmDto savedFilm = filmController.save(film);
        filmController.addLike(savedFilm.getId(), savedUser.getId());
        //TO DO нет эндпоинтов на получение лайков на фильме - дописать
    }

    @Test
    @DisplayName("Проверка PUT повторного like на правильном фильме и пользователе")
    void testAddSecondLike() {
        UserDto savedUser = userController.save(user);
        FilmDto savedFilm = filmController.save(film);
        filmController.addLike(savedFilm.getId(), savedUser.getId());
        filmController.addLike(savedFilm.getId(), savedUser.getId());
        //TO DO: нет эндпоинтов на получение лайков на фильме - дописать
    }

    @Test
    @DisplayName("Проверка DELETE like на неправильном фильме")
    void testDeleteLikeWrongFilm() {
        UserDto savedUser = userController.save(user);
        FilmDto savedFilm = filmController.save(film);
        filmController.addLike(savedFilm.getId(), savedUser.getId());
        try {
            filmController.removeLike(null, savedUser.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE like на неправильном пользователе")
    void testDeleteLikeWrongUser() {
        UserDto savedUser = userController.save(user);
        FilmDto savedFilm = filmController.save(film);
        filmController.addLike(savedFilm.getId(), savedUser.getId());
        try {
            filmController.removeLike(savedFilm.getId(), null);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE like на правильном фильме, правильном пользователе и отсутствующем like")
    void testDeleteLikeRightUserRightFilmNoLike() {
        UserDto savedUser = userController.save(user);
        UserDto savedUser1 = userController.save(user1);
        FilmDto savedFilm = filmController.save(film);
        filmController.addLike(savedFilm.getId(), savedUser.getId());
        filmController.removeLike(savedFilm.getId(), savedUser1.getId());
        //TO DO нет эндпоинтов на получение лайков на фильме - дописать
    }

    @Test
    @DisplayName("Проверка DELETE like на правильном пользователе и фильме")
    void testDeleteLike() {
        UserDto savedUser = userController.save(user);
        FilmDto savedFilm = filmController.save(film);
        filmController.addLike(savedFilm.getId(), savedUser.getId());
        filmController.removeLike(savedFilm.getId(), savedUser.getId());
        //TO DO нет эндпоинтов на получение лайков на фильме - дописать
    }

    @Test
    @DisplayName("Проверка GET popular на фильмах без лайков")
    void testFindMostRatedNoLikes() {
        filmController.save(film);
        filmController.save(film1);
        filmController.save(film2);
        filmController.save(film3);
        filmController.save(film4);
        List<FilmDto> films = filmController.findMostRated(5);
        Assertions.assertAll(
                () -> Assertions.assertEquals(5, films.size())
        );
    }

    @Test
    @DisplayName("Проверка GET popular на фильмах с лайками выборка меньше количества фильмов")
    void testFindMostRatedLikesCount3() {
        FilmDto savedFilm = filmController.save(film);
        FilmDto savedFilm1 = filmController.save(film1);
        FilmDto savedFilm2 = filmController.save(film2);
        FilmDto savedFilm3 = filmController.save(film3);
        FilmDto savedFilm4 = filmController.save(film4);
        UserDto savedUser = userController.save(user);
        UserDto savedUser1 = userController.save(user1);
        UserDto savedUser2 = userController.save(user2);
        UserDto savedUser3 = userController.save(user3);
        UserDto savedUser4 = userController.save(user4);

        filmController.addLike(savedFilm.getId(), savedUser.getId());
        filmController.addLike(savedFilm.getId(), savedUser1.getId());
        filmController.addLike(savedFilm.getId(), savedUser2.getId());
        filmController.addLike(savedFilm.getId(), savedUser3.getId());
        filmController.addLike(savedFilm.getId(), savedUser4.getId());

        filmController.addLike(savedFilm1.getId(), savedUser.getId());
        filmController.addLike(savedFilm1.getId(), savedUser1.getId());
        filmController.addLike(savedFilm1.getId(), savedUser2.getId());
        filmController.addLike(savedFilm1.getId(), savedUser3.getId());

        filmController.addLike(savedFilm2.getId(), savedUser.getId());
        filmController.addLike(savedFilm2.getId(), savedUser1.getId());
        filmController.addLike(savedFilm2.getId(), savedUser2.getId());

        filmController.addLike(savedFilm3.getId(), savedUser.getId());
        filmController.addLike(savedFilm3.getId(), savedUser1.getId());

        filmController.addLike(savedFilm4.getId(), savedUser.getId());

        List<FilmDto> films3 = filmController.findMostRated(3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(3, films3.size()),
                () -> Assertions.assertEquals(savedFilm.getId(), films3.getFirst().getId()),
                () -> Assertions.assertEquals(savedFilm2.getId(), films3.getLast().getId())
        );

        List<FilmDto> films10 = filmController.findMostRated(10);
        Assertions.assertAll(
                () -> Assertions.assertEquals(5, films10.size()),
                () -> Assertions.assertEquals(savedFilm.getId(), films10.getFirst().getId()),
                () -> Assertions.assertEquals(savedFilm4.getId(), films10.getLast().getId())
        );
    }

    @Test
    @DisplayName("Проверка POST на фильме с рейтингом не из списка")
    void testSaveFilmWrongRating() {
        film.getMpa().setId(10);
        try {
            filmController.save(film);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Рейтинг с id = 10 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка POST на фильме с жанром не из списка")
    void testSaveFilmWrongGenre() {
        GenreDto genre = new GenreDto().setId(999).setName("wrong genre");
        film.getGenres().add(genre);
        try {
            filmController.save(film);
        } catch (NotFoundException e) {
            Assertions.assertEquals("Жанр с id = 999 не найден", e.getMessage());
        }
    }
}
