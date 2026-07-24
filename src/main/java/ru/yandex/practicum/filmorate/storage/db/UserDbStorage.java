package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Qualifier("UserDbStorage")
public class UserDbStorage extends BaseStorage<User> implements UserStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO USERS(EMAIL,LOGIN,USERNAME,BIRTHDAY) " +
                                                    "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE USERS " +
                                                    "SET EMAIL = ?, LOGIN = ?, USERNAME = ?, BIRTHDAY = ? " +
                                                    "WHERE USER_ID = ?";
    private static final String DELETE_ALL_QUERY = "DELETE FROM USERS";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM USERS WHERE EMAIL = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO FRIENDS_RELATION(USER_ID,FRIEND_ID) " +
                                                      "VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM FRIENDS_RELATION WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USERNAME, U.BIRTHDAY " +
                                                         "FROM FRIENDS_RELATION FR " +
                                                         "JOIN USERS U ON U.USER_ID = FR.FRIEND_ID " +
                                                         "WHERE FR.USER_ID = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT DISTINCT U.USER_ID, U.EMAIL, U.LOGIN, U.USERNAME, U.BIRTHDAY " +
                                                            "FROM FRIENDS_RELATION FR " +
                                                            "JOIN USERS U " +
                                                            "ON U.USER_ID = FR.FRIEND_ID " +
                                                            "WHERE FR.USER_ID = ? " +
                                                            "AND FR.FRIEND_ID IN (SELECT FRO.FRIEND_ID" +
                                                            "                       FROM FRIENDS_RELATION FRO " +
                                                            "                      WHERE FRO.USER_ID = ?)";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User save(User user) {
        Integer id = insert(
                INSERT_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public void addFriend(User user, User friend) {
        insert(INSERT_FRIEND_QUERY,user.getId(),friend.getId());
    }

    @Override
    public void removeFriend(User user, User friend) {
        deleteByParam(DELETE_FRIEND_QUERY, user.getId(), friend.getId());
    }

    @Override
    public List<User> findAllFriends(User user) {
        return findMany(FIND_ALL_FRIENDS_QUERY, user.getId());
    }

    @Override
    public Set<User> findCommonFriends(User user, User otherUser) {
        List<User> users = findMany(FIND_COMMON_FRIENDS_QUERY, user.getId(), otherUser.getId());
        return new HashSet<>(users);
    }

    @Override
    public void deleteAll() {
        delete(DELETE_ALL_QUERY);
    }

    @Override
    public boolean existEmail(String email) {
        Optional<User> user = findOne(FIND_BY_EMAIL_QUERY, email);
        return user.isPresent();
    }
}
