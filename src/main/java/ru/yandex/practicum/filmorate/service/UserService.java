package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewUserDto;
import ru.yandex.practicum.filmorate.dto.UpdateUserDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.enumeration.EventType;
import ru.yandex.practicum.filmorate.enumeration.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.LikesRelation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FilmService filmService;
    private final LikesRelationService likesRelationService;
    private final EventService eventService;

    public List<UserDto> findAll() {
        return userStorage.findAll().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public UserDto findById(Integer id) {
        return UserMapper.mapToUserDto(findUserById(id));
    }

    public UserDto save(NewUserDto newUserDto) {
        log.info("Saving user {}", newUserDto);
        User user = UserMapper.mapToUser(newUserDto);
        replaceName(user);
        log.debug("Replaced userName for save {}", user.getName());
        validateEmail(user.getEmail());
        User newUser = userStorage.save(user);
        return UserMapper.mapToUserDto(newUser);
    }

    public UserDto update(UpdateUserDto updateUserDto) {
        log.info("Updating newUser {}", updateUserDto);
        User oldUser = findUserById(updateUserDto.getId());
        log.debug("Old user for update {}", oldUser);
        validateEmail(updateUserDto.getEmail());
        //сливаем воедино пришедшие данные и данные в базе-формируем целиком юзера
        User newUser = UserMapper.updateUserFields(oldUser, updateUserDto);
        User updatedUser = userStorage.update(newUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public UserDto addFriend(Integer userId, Integer friendId) {
        log.info("Adding friend {} to User {}", friendId, userId);
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        if (Objects.equals(userId, friendId)) {
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }
        userStorage.addFriend(user, friend);
        eventService.createEvent(userId, EventType.FRIEND, Operation.ADD, friendId);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto removeFriend(Integer userId, Integer friendId) {
        log.info("Removing friend {} from User {}", friendId, userId);
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        userStorage.removeFriend(user, friend);
        eventService.createEvent(userId, EventType.FRIEND, Operation.REMOVE, friendId);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> findAllFriends(Integer userId) {
        log.info("Finding all friends from User {}", userId);
        User user = findUserById(userId);
        return userStorage.findAllFriends(user).stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public Set<UserDto> findCommonFriends(Integer id, Integer otherId) {
        log.info("Finding common friends from User {} to User {}", id, otherId);
        User user = findUserById(id);
        User otherUser = findUserById(otherId);
        return userStorage.findCommonFriends(user, otherUser).stream().map(UserMapper::mapToUserDto).collect(Collectors.toSet());
    }

    public void deleteUser(Integer userId) {
        userStorage.deleteById(userId);
        log.info("Удаление пользователя с id = {}", userId);
    }

    public List<FilmDto> getRecommendations(Integer userId) {
        log.info("Поиск рекомендация по фильмам для userId = {} ", userId);
        List<FilmDto> recommendedFilms = new ArrayList<>();
        Collection<LikesRelation> likes = likesRelationService.get();
        if (likes.isEmpty()) {  //нет лайков - сразу уходим
            return recommendedFilms;
        }
        //забираем уникальный набор пользователей - в списках лайках может быть дублирование
        Set<Integer> users = likes.stream().map(LikesRelation::getUserId).collect(Collectors.toSet());
        if (!users.contains(userId) || users.size() == 1) { //наш клиент ничего не лайкал или лайкал только он - на выход
            return recommendedFilms;
        }
        //забираем уникальный набор фильмов - в списках лайках может быть дублирование
        Set<Integer> films = likes.stream().map(LikesRelation::getFilmId).collect(Collectors.toSet());
        //набираем мапу существующих лайков
        Map<Integer, Set<Integer>> likesMap = getLikesMap(likes);
        //набираем матрицу все пользователи-все фильмы-оценки
        Map<Integer, HashMap<Integer, Integer>> rates = getRates(likesMap, films);
        //Забираем данные нашего пользователя и убираем из общего расчета
        HashMap<Integer, Integer> ourUserRates = rates.get(userId);
        rates.remove(userId);
        //посчитаем для него сразу длину (евклидову норму)
        double ourUserLength = calcLength(ourUserRates.values());
        //считаем косинусное сходство для каждого пользователя в сравнении с нашим пользователем
        Map<Integer, Double> cosineSimilarity = getCosineSimilarity(rates, ourUserRates, ourUserLength);
        //ищем самого похожего пользователя
        Integer alterUserId = getSimilarUserId(cosineSimilarity);
        if (alterUserId != null) {  //есть такой пользователь
            //набираем id фильмов которые ЕСТЬ у второго пользователя и НЕТ у нашего пользователя
            Set<Integer> notMatchedIds = likesMap.get(alterUserId)
                    .stream()
                    .filter(id -> !likesMap.get(userId).contains(id))
                    .collect(Collectors.toSet());
            if (!notMatchedIds.isEmpty()) {  //есть хоть что-то что можем рекомендовать
                recommendedFilms = filmService.getByIds(notMatchedIds);
            }
        }
        return recommendedFilms;
    }

    private static Integer getSimilarUserId(Map<Integer, Double> cosineSimilarity) {
        log.info("Поиск похожего пользователя");
        Integer alterUserId = null;
        //выбираем максимальный коэффициент сходства
        Double maxCosine = cosineSimilarity.values().stream().max(Double::compareTo).orElse(0.0);
        //находим пользователя с этим максимальным коэффициентом - по условиям задачи берем только одного
        if (maxCosine > 0.0) {  //берем только с ненулевым коэффициентом
            Optional<Map.Entry<Integer, Double>> alterUserIdOpt = cosineSimilarity.entrySet()
                    .stream()
                    .filter(e -> Objects.equals(e.getValue(), maxCosine))
                    .findFirst();
            if (alterUserIdOpt.isPresent()) {
                alterUserId = alterUserIdOpt.get().getKey();
            }
        }
        return alterUserId;
    }

    private static Map<Integer, Set<Integer>> getLikesMap(Collection<LikesRelation> likes) {
        log.info("Получение мапы лайков");
        Map<Integer, Set<Integer>> likesMap = new HashMap<>();
        likes.forEach(like -> {
            Set<Integer> filmIds = likesMap.computeIfAbsent(like.getUserId(), k -> new HashSet<>());
            filmIds.add(like.getFilmId());
        });
        return likesMap;
    }

    private static Map<Integer, HashMap<Integer, Integer>> getRates(Map<Integer, Set<Integer>> likesMap, Set<Integer> films) {
        log.info("Получение матрицы оценок");
        Map<Integer, HashMap<Integer, Integer>> rates = new HashMap<>();
        likesMap.forEach((k, v) -> {
            HashMap<Integer, Integer> filmRates = rates.computeIfAbsent(k, k1 -> new HashMap<>());
            //записываем фильм с лайком с коэффициентом 1
            v.forEach(filmId -> filmRates.put(filmId, 1));
            //записываем фильм без лайка с коэффициентом 0
            films.stream()
                    .filter(filmId -> filmRates.get(filmId) == null)
                    .forEach(filmId -> filmRates.put(filmId, 0));
        });
        return rates;
    }

    private static Map<Integer, Double> getCosineSimilarity(Map<Integer, HashMap<Integer, Integer>> rates,
                                                            HashMap<Integer, Integer> ourUserRates,
                                                            double ourUserLength) {
        log.info("Расчет косинусного сходства");
        Map<Integer, Double> cosineSimilarity = new HashMap<>();
        rates.forEach((k, v) -> {
            double cosine = 0;
            int scale = 0;
            //считаем скалярное произведение векторов
            for (Map.Entry<Integer, Integer> rate : v.entrySet()) {
                //перемножаем оценки по каждому фильму у нашего пользователя и того пользователя, которого обрабатываем
                int pair = rate.getValue() * ourUserRates.get(rate.getKey());
                scale = scale + pair;
            }
            //считаем длину вектора этого пользователя
            double length = calcLength(v.values());
            //если длины не нули, то считаем косинусное сходство
            if (ourUserLength != 0 && length != 0) {
                cosine = scale / (length * ourUserLength);
            }
            cosineSimilarity.put(k, cosine);
        });
        return cosineSimilarity;
    }

    private static double calcLength(Collection<Integer> rates) {
        log.info("Расчет длины векторов (эвклидовы нормы)");
        double ourUserLength = 0.0;
        for (Integer rate : rates) {
            ourUserLength = ourUserLength + (rate * rate);
        }
        ourUserLength = Math.sqrt(ourUserLength);
        return ourUserLength;
    }

    private User findUserById(Integer id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    private static void replaceName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    private void validateEmail(String email) {
        if (email != null && !email.isBlank() && userStorage.existEmail(email)) {
            throw new ValidationException("Пользователь с такой почтой уже существует!");
        }
    }
}
