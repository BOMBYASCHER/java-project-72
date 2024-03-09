DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    created_at timestamp
);
