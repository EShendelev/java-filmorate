--DELETE FROM likes;
--DELETE FROM film_genre;
--DELETE FROM film_directors;
--DELETE FROM friends;
--DELETE FROM users;
--DELETE FROM films;
--DELETE FROM directors;
--DELETE FROM reviews;
--DELETE FROM events;
--ALTER TABLE users ALTER COLUMN id RESTART with 1;
--ALTER TABLE films ALTER COLUMN id RESTART with 1;
--ALTER TABLE directors ALTER COLUMN id RESTART with 1;
--ALTER TABLE reviews ALTER COLUMN ID RESTART with 1;
--ALTER TABLE events ALTER COLUMN event_id RESTART with 1;

MERGE INTO mpa_rating (id, name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');
           
MERGE INTO genres (id, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');