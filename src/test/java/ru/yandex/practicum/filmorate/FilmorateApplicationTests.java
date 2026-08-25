package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:schema.sql", "classpath:data.sql"})
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate.storage")
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final MpaRatingDBStorage mpaRatingStorage;
    private final FilmDbStorage filmStorage;
    private final LikesRelationDBStorage likesRelationDBStorage;
    private static User user;
    private static User user1;
    private static User user2;
    private static Film film;
    private static Film film1;

    @BeforeEach
    void beforeEach() {
        MpaRating mpaRating = new MpaRating().setId(1);
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
                .setBirthday(LocalDate.of(1990, 1, 1));
        film = new Film()
                .setName("film")
                .setDescription("description")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(200)
                .setMpa(mpaRating);
        film1 = new Film()
                .setName("film1")
                .setDescription("description1")
                .setReleaseDate(LocalDate.of(1990, 1, 1))
                .setDuration(100)
                .setMpa(mpaRating);
    }

    @Test
    void testFindUserById() {
        User savedUser = userStorage.save(user);
        Optional<User> userOptional = userStorage.findById(savedUser.getId());
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(savedUser.getId(), user.getId());
    }

    @Test
    void testFindAllUsers() {
        userStorage.save(user);
        userStorage.save(user1);
        List<User> users = userStorage.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testAddFriends() {
        User savedUser = userStorage.save(user);
        User savedUser1 = userStorage.save(user1);
        userStorage.addFriend(savedUser, savedUser1);
        List<User> friends = userStorage.findAllFriends(savedUser);
        assertEquals(1, friends.size());
        assertEquals(savedUser1, friends.getFirst());
    }

    @Test
    void testRemoveFriends() {
        User savedUser = userStorage.save(user);
        User savedUser1 = userStorage.save(user1);
        userStorage.addFriend(savedUser, savedUser1);
        userStorage.removeFriend(savedUser, savedUser1);
        List<User> friends = userStorage.findAllFriends(savedUser);
        assertEquals(0, friends.size());
    }

    @Test
    void testFindCommonFriends() {
        User savedUser = userStorage.save(user);
        User savedUser1 = userStorage.save(user1);
        User savedUser2 = userStorage.save(user2);
        userStorage.addFriend(savedUser, savedUser1);
        userStorage.addFriend(savedUser2, savedUser1);
        Set<User> friends = userStorage.findCommonFriends(savedUser, savedUser2);
        assertEquals(1, friends.size());
        assertTrue(friends.contains(savedUser1));
    }

    @Test
    void testDelete() {
        userStorage.save(user);
        userStorage.save(user1);
        userStorage.save(user2);
        userStorage.deleteAll();
        List<User> users = userStorage.findAll();
        assertEquals(0, users.size());
    }

    @Test
    void testUpdate() {
        User savedUser = userStorage.save(user);
        savedUser.setName("updated");
        userStorage.update(savedUser);
        Optional<User> userOptional = userStorage.findById(savedUser.getId());
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(savedUser.getId(), user.getId());
        assertEquals(savedUser.getName(), user.getName());
    }

    @Test
    void testExistEmail() {
        User savedUser = userStorage.save(user);
        assertTrue(userStorage.existEmail(savedUser.getEmail()));
    }

    @Test
    void testFindFilmById() {
        Film savedFilm = filmStorage.save(film);
        Optional<Film> filmOptional = filmStorage.findById(savedFilm.getId());
        assertTrue(filmOptional.isPresent());
        Film film = filmOptional.get();
        assertEquals(savedFilm.getId(), film.getId());
    }

    @Test
    void testFindAllFilms() {
        filmStorage.save(film);
        filmStorage.save(film1);
        List<Film> films = filmStorage.findAll();
        assertEquals(2, films.size());
    }

    @Test
    void testDeleteAllFilms() {
        filmStorage.save(film);
        filmStorage.save(film1);
        filmStorage.deleteAll();
        List<Film> films = filmStorage.findAll();
        assertEquals(0, films.size());
    }

    @Test
    void testFilmUpdate() {
        Film savedFilm = filmStorage.save(film);
        savedFilm.setName("updated");
        filmStorage.update(savedFilm);
        Optional<Film> filmOptional = filmStorage.findById(savedFilm.getId());
        assertTrue(filmOptional.isPresent());
        Film film = filmOptional.get();
        assertEquals(savedFilm.getId(), film.getId());
        assertEquals(savedFilm.getName(), film.getName());
    }

    @Test
    void testFilmAddRemoveLike() {
        Film savedFilm = filmStorage.save(film);
        User savedUser = userStorage.save(user);
        likesRelationDBStorage.addLike(savedFilm, savedUser.getId());
        assertEquals(1, likesRelationDBStorage.countLikesByFilmId(savedFilm.getId()));
        likesRelationDBStorage.removeLike(savedFilm, savedUser.getId());
        assertEquals(0, likesRelationDBStorage.countLikesByFilmId(savedFilm.getId()));
    }

    @Test
    void testFindAllGenres() {
        List<Genre> genres = genreStorage.findAll();
        assertEquals(6, genres.size());
    }

    @Test
    void testFindGenreById() {
        Optional<Genre> genreOptional = genreStorage.findById(1);
        assertTrue(genreOptional.isPresent());
        Genre genre = genreOptional.get();
        assertEquals(1, genre.getId());
    }

    @Test
    void testFindAllRatings() {
        List<MpaRating> ratings = mpaRatingStorage.findAll();
        assertEquals(5, ratings.size());
    }

    @Test
    void testFindRatingById() {
        Optional<MpaRating> ratingOptional = mpaRatingStorage.findById(1);
        assertTrue(ratingOptional.isPresent());
        MpaRating rating = ratingOptional.get();
        assertEquals(1, rating.getId());
    }
}
