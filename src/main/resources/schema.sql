CREATE TABLE IF NOT EXISTS users (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	login VARCHAR(25) NOT NULL,
	email VARCHAR(50) NOT NULL,
	name VARCHAR(75) NOT NULL,
	birthday DATE
);

CREATE TABLE IF NOT EXISTS friends (
	user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
	friend_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
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
	mpa_id INTEGER REFERENCES mpa_rating (id)
);

CREATE TABLE IF NOT EXISTS genres (
	id INTEGER PRIMARY KEY,
	name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
	genre_id INTEGER REFERENCES genres (id),
	PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
	film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
	user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
	PRIMARY KEY(film_id, user_id)
);

CREATE TABLE IF NOT EXISTS directors (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_directors (
	film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
	director_id INTEGER REFERENCES directors (id) ON DELETE CASCADE,
	PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS reviews (
     id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     content VARCHAR(250) NOT NULL,
     is_positive BOOLEAN,
     user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
     film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
     useful INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS user_review_reactions
(
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    review_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
    is_like BOOLEAN
);

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_time BIGINT,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    entity_id BIGINT,
    event_type VARCHAR(10),
    operation VARCHAR(10)
);