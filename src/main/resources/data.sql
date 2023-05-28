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
           (4, 'Фантастика'),
           (5, 'Триллер'),
           (6, 'Боевик');
           
MERGE INTO status_code (code, name)
	VALUES ('R', 'Requested'),
			('A', 'Accepted'),
			('D', 'Declined'),
			('B', 'Blocked');