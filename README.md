# java-filmorate
Приложение Filmorate - оценка фильмов
## База Данных
### Список таблиц базы данных
1. Таблица users содержит данные о пользователях
2. Таблица films содержит данные о фильмах
3. Таблица ratings содержит данные о рейтингах фильмов
4. Таблица genres содержит данные о жанрах фильмов
5. Таблица friends_relation содержит данные о дружбе между пользователями
6. Таблица genres_relation содержит данные о рейтингах конкретных фильмов
7. Таблица likes_relation содержит данные о лайках пользователей фильмам
### ER-диаграмма
![Схема базы данных filmorate-db](/src/main/resources/filmorate_db.png)


### Примеры запросов к базе данных
1. Отобразить всех пользователей
```sql
select * from users
```
2. Отобразть данные пользователя по id пользователя
```sql
select * from users where user_id = 1
```
3. Отобразить id всех друзей пользователя по id пользователя
```sql
select * from friends_relation  where user_id = 1
```
4. Отобразить все жанры
```sql
select * from genres
```
5. Отобразить данные жанра по id жанра
```sql
select * from genres where genre_id = 1
```
6. Отобразить все рейтинги
```sql
select * from ratings
```
7. Отобразить данные рейтинга по id рейтинга
```sql
select * from ratings where rating_id = 1
```
8. Отобразить все фильмы
```sql
select * from films
```
9. Отобразить данные фильма по id фильма
```sql
select * from films where film_id = 1
```
10. Отобразить id всех фильмов по id жанра
```sql
select * from genres_relation  where genre_id = 1
```
11. Отобразить id всех жанров фильма по id фильма
```sql
select * from genres_relation  where film_id = 1
```
12. Отобразить id всех пользователей лайкнувших фильм по id фильма
```sql
select * from likes_relation where film_id  = 1
```
