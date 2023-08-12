
CREATE TABLE IF NOT EXISTS users (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	login VARCHAR(25) NOT NULL,
	email VARCHAR(50) NOT NULL,
	name VARCHAR(75) NOT NULL,
	birthday DATE CHECK (birthday <= CURRENT_DATE()),
	CONSTRAINT login_is_not_empty CHECK (login <> ''),
	CONSTRAINT no_spaces_in_login CHECK (login NOT IN (' '))
);

CREATE TABLE IF NOT EXISTS friends (
	user_id BIGINT REFERENCES users (id),
	friend_id BIGINT REFERENCES users (id),
	PRIMARY KEY (user_id, friend_id)
);


CREATE TABLE IF NOT EXISTS mpa_rating (
	id INTEGER PRIMARY KEY,
	name VARCHAR(30) NOT NULL 
);

CREATE TABLE IF NOT EXISTS films (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	description VARCHAR(250),
	release_date DATE,
	duration INTEGER,
	rate INTEGER,
	mpa_id INTEGER REFERENCES mpa_rating (id),
	CONSTRAINT name_is_not_empty CHECK (name <> ''),
	CONSTRAINT duration_always_positive CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS genres (
	id INTEGER PRIMARY KEY,
	name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id BIGINT REFERENCES films (id),
	genre_id INTEGER REFERENCES genres (id),
	PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
	film_id BIGINT REFERENCES films (id),
	user_id BIGINT REFERENCES users (id),
	PRIMARY KEY(film_id, user_id)
);



