delete from likes;
delete from film_genre;
delete from friends;
delete from users;
delete from films;
alter table users alter COLUMN id RESTART with 1;
alter table films alter COLUMN id RESTART with 1;

merge into mpa_rating (id, name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');
           
merge into genres (id, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');