package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
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
    private static User user;
    private static User user1;
    private static User user2;
    private static User newUser;
    private static Film film;
    private static Film film1;
    private static Film film2;
    private static Film film3;
    private static Film film4;
    private static Film newFilm;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(userService);
        filmController = new FilmController(filmService);
        filmService.deleteAll();
        userService.deleteAll();
        user = new User()
                .setLogin("user")
                .setName("user")
                .setEmail("email@ya.ru")
                .setBirthday(LocalDate.of(1990, 1, 1));
        user1 = new User()
                .setLogin("user1")
                .setName("user1")
                .setEmail("email1@ya.ru")
                .setBirthday(LocalDate.of(1990, 1, 1));
        user2 = new User()
                .setLogin("user2")
                .setName("user2")
                .setEmail("email2@ya.ru")
                .setBirthday(LocalDate.of(1958, 1, 1));
        newUser = new User()
                .setLogin("newLogin")
                .setName("newName")
                .setEmail("newEmail@ya.ru")
                .setBirthday(LocalDate.of(2000, 1, 1));
        film = new Film()
                .setName("film")
                .setDescription("description")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(200);
        film1 = new Film()
                .setName("film1")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100);
        film2 = new Film()
                .setName("film2")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100);
        film3 = new Film()
                .setName("film3")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100);
        film4 = new Film()
                .setName("film4")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100);
        newFilm = new Film()
                .setName("newName")
                .setDescription("newLogin")
                .setReleaseDate(LocalDate.of(2000, 1, 1))
                .setDuration(500);
    }

    //ТЕСТЫ НА USER
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
        newUser.setId(user.getId());
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
        } catch (NotFoundException e) {
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
        } catch (NotFoundException e) {
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
    @DisplayName("Проверка GET на отсутствующем пользователе")
    void testGetUserByIDWithNoUser() {
        try {
            userController.findById(user1.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET на неверном id пользователя")
    void testGetUserByIDWithWrongId() {
        userController.save(user);
        user1.setId(999999);
        try {
            userController.findById(user1.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET на правильном id пользователя")
    void testGetUserByIdWithRightId() {
        userController.save(user);
        User savedUser = userController.findById(user.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(user.getName(), savedUser.getName()),
                () -> Assertions.assertEquals(user.getLogin(), savedUser.getLogin()),
                () -> Assertions.assertEquals(user.getEmail(), savedUser.getEmail()),
                () -> Assertions.assertEquals(user.getBirthday(), savedUser.getBirthday())
        );
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей при несущетсвующем пользователе")
    void testAddFriendNoUser() {
        try {
            userController.addFriend(user.getId(), user1.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей при несущетсвующем друге")
    void testAddFriendNoFriend() {
        userController.save(user);
        try {
            userController.addFriend(user.getId(), user1.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей пользователь дружит с самим собой")
    void testAddFriendFriendYourself() {
        userController.save(user);
        try {
            userController.addFriend(user.getId(), user.getId());
        } catch (ValidationException e) {
            Assertions.assertEquals("Нельзя добавить в друзья самого себя", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT добавления друзей правильный пользователь и друг")
    void testAddFriendRightUserRightFriend() {
        userController.save(user);
        userController.save(user1);
        userController.addFriend(user.getId(), user1.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, user.getFriends().size()),
                () -> Assertions.assertTrue(user.getFriends().contains(user1.getId()))
        );
    }

    @Test
    @DisplayName("Проверка DELETE друзей при несущетсвующем пользователе")
    void testDeleteFriendWrongUser() {
        try {
            userController.removeFriend(user.getId(), user.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE друзей при несущетсвующем друге")
    void testDeleteFriendWrongFriend() {
        userController.save(user);
        try {
            userController.removeFriend(user.getId(), user.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE друзей правильный пользователь и друг")
    void testDeleteFriendRightUserRightFriend() {
        userController.save(user);
        userController.save(user1);
        userController.removeFriend(user.getId(), user1.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, user.getFriends().size()),
                () -> Assertions.assertEquals(0, user1.getFriends().size()),
                () -> Assertions.assertFalse(user.getFriends().contains(user1.getId())),
                () -> Assertions.assertFalse(user1.getFriends().contains(user.getId()))
        );
    }

    @Test
    @DisplayName("Проверка GET всех друзей неправильный пользователь")
    void testFindAllFriendsWrongUser() {
        try {
            userController.findAllFriends(user.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET всех друзей правильный пользователь без друзей")
    void testFindAllFriendsNoFriend() {
        userController.save(user);
        List<User> friends = userController.findAllFriends(user.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, friends.size())
        );
    }

    @Test
    @DisplayName("Проверка GET всех друзей правильный пользователь с друзьями")
    void testFindAllFriends() {
        userController.save(user);
        userController.save(user1);
        userController.addFriend(user.getId(), user1.getId());
        List<User> friends = userController.findAllFriends(user.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, friends.size()),
                () -> Assertions.assertEquals(friends.getFirst(), user1)
        );
    }

    @Test
    @DisplayName("Проверка GET общих друзей неправильный пользователь")
    void testFindCommonFriendWrongUser() {
        try {
            userController.findCommonFriends(user.getId(), user2.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET общих друзей неправильный друг")
    void testFindCommonFriendWrongFriend() {
        userController.save(user);
        try {
            userController.findCommonFriends(user.getId(), user2.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET общих друзей без общих друзей")
    void testFindCommonFriendNoCommon() {
        userController.save(user);
        userController.save(user1);
        userController.save(user2);
        userController.addFriend(user.getId(), user1.getId());
        Set<User> friends = userController.findCommonFriends(user.getId(), user2.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, friends.size())
        );
    }

    @Test
    @DisplayName("Проверка GET общих друзей с общими друзьями")
    void testFindCommonFriend() {
        userController.save(user);
        userController.save(user1);
        userController.save(user2);
        userController.addFriend(user.getId(), user1.getId());
        userController.addFriend(user2.getId(), user1.getId());
        Set<User> friends = userController.findCommonFriends(user.getId(), user2.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, friends.size()),
                () -> Assertions.assertEquals(friends.iterator().next(), user1)
        );
    }

    //ТЕСТЫ НА FILM
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
        newFilm.setId(film.getId());
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
        } catch (NotFoundException e) {
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
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT на фильме c неверной датой")
    void testUpdateFilmWithWrongDate() {
        filmController.save(film);
        newFilm.setId(film.getId());
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
            filmController.findById(film.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET BY ID на неверном фильме")
    void testFindByIdWrongFilm() {
        filmController.save(film);
        film1.setId(999999);
        try {
            filmController.findById(film1.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = 999999 не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка GET BY ID на правильном фильме")
    void testFindById() {
        filmController.save(film);
        Film findedFilm = filmController.findById(film.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(film.getName(), findedFilm.getName()),
                () -> Assertions.assertEquals(film.getDescription(), findedFilm.getDescription()),
                () -> Assertions.assertEquals(film.getReleaseDate(), findedFilm.getReleaseDate()),
                () -> Assertions.assertEquals(film.getDuration(), findedFilm.getDuration())
        );
    }

    @Test
    @DisplayName("Проверка PUT like на неправильном фильме")
    void testAddLikeNoFilm() {
        userController.save(user);
        try {
            filmController.addLike(film.getId(), user.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT like на неправильном пользователе")
    void testAddLikeNoUser() {
        filmController.save(film);
        try {
            filmController.addLike(film.getId(), user.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка PUT like на правильном фильме и пользователе")
    void testAddLike() {
        userController.save(user);
        filmController.save(film);
        Film savedFilm = filmController.addLike(film.getId(), user.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, savedFilm.getLikes().size()),
                () -> Assertions.assertTrue(savedFilm.getLikes().contains(user.getId()))
        );
    }

    @Test
    @DisplayName("Проверка PUT повторного like на правильном фильме и пользователе")
    void testAddSecondLike() {
        userController.save(user);
        filmController.save(film);
        filmController.addLike(film.getId(), user.getId());
        Film savedFilm1 = filmController.addLike(film.getId(), user.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, savedFilm1.getLikes().size()),
                () -> Assertions.assertTrue(savedFilm1.getLikes().contains(user.getId()))
        );
    }

    @Test
    @DisplayName("Проверка DELETE like на неправильном фильме")
    void testDeleteLikeWrongFilm() {
        userController.save(user);
        filmController.save(film);
        filmController.addLike(film.getId(), user.getId());
        try {
            filmController.removeLike(film1.getId(), user.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Фильм с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE like на неправильном пользователе")
    void testDeleteLikeWrongUser() {
        userController.save(user);
        filmController.save(film);
        filmController.addLike(film.getId(), user.getId());
        try {
            filmController.removeLike(film.getId(), user1.getId());
        } catch (NotFoundException e) {
            Assertions.assertEquals("Пользователь с id = null не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка DELETE like на правильном фильме, правильном пользователе и отсутствующем like")
    void testDeleteLikeRightUserRightFilmNoLike() {
        userController.save(user);
        userController.save(user1);
        filmController.save(film);
        filmController.addLike(film.getId(), user.getId());
        Film savedFilm1 = filmController.removeLike(film.getId(), user1.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, savedFilm1.getLikes().size()),
                () -> Assertions.assertFalse(savedFilm1.getLikes().contains(user1.getId())),
                () -> Assertions.assertTrue(savedFilm1.getLikes().contains(user.getId()))
        );
    }

    @Test
    @DisplayName("Проверка DELETE like на правильном пользователе и фильме")
    void testDeleteLike() {
        userController.save(user);
        filmController.save(film);
        filmController.addLike(film.getId(), user.getId());
        Film savedFilm1 = filmController.removeLike(film.getId(), user.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, savedFilm1.getLikes().size()),
                () -> Assertions.assertFalse(savedFilm1.getLikes().contains(user.getId()))
        );
    }

    @Test
    @DisplayName("Проверка GET popular на фильмах без лайков")
    void testFindMostRatedNoLikes() {
        filmController.save(film);
        filmController.save(film1);
        filmController.save(film2);
        filmController.save(film3);
        filmController.save(film4);
        List<Film> films = filmController.findMostRated(5);
        Assertions.assertAll(
                () -> Assertions.assertEquals(5, films.size())
        );
    }

    @Test
    @DisplayName("Проверка GET popular на фильмах с лайками выборка меньше количества фильмов")
    void testFindMostRatedLikesCount3() {
        filmController.save(film);
        filmController.save(film1);
        filmController.save(film2);
        filmController.save(film3);
        filmController.save(film4);
        Set<Integer> likes = Set.of(1, 2, 3, 4, 5);
        Set<Integer> likes1 = Set.of(1, 2, 3, 4);
        Set<Integer> likes2 = Set.of(1, 2, 3);
        Set<Integer> likes3 = Set.of(1, 2);
        Set<Integer> likes4 = Set.of(1);

        film.setLikes(likes);
        film1.setLikes(likes1);
        film2.setLikes(likes2);
        film3.setLikes(likes3);
        film4.setLikes(likes4);

        List<Film> films = filmController.findMostRated(3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(3, films.size()),
                () -> Assertions.assertEquals(film, films.getFirst()),
                () -> Assertions.assertEquals(film2, films.getLast())
        );
    }

    @Test
    @DisplayName("Проверка GET popular на фильмах с лайками выборка больше количества фильмов")
    void testFindMostRatedLikesCount10() {
        filmController.save(film);
        filmController.save(film1);
        filmController.save(film2);
        filmController.save(film3);
        filmController.save(film4);
        Set<Integer> likes = Set.of(1, 2, 3, 4, 5);
        Set<Integer> likes1 = Set.of(1, 2, 3, 4);
        Set<Integer> likes2 = Set.of(1, 2, 3);
        Set<Integer> likes3 = Set.of(1, 2);
        Set<Integer> likes4 = Set.of(1);

        film.setLikes(likes);
        film1.setLikes(likes1);
        film2.setLikes(likes2);
        film3.setLikes(likes3);
        film4.setLikes(likes4);

        List<Film> films = filmController.findMostRated(10);
        Assertions.assertAll(
                () -> Assertions.assertEquals(5, films.size()),
                () -> Assertions.assertEquals(film, films.getFirst()),
                () -> Assertions.assertEquals(film4, films.getLast())
        );
    }
}
