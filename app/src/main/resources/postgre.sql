DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar(255) NOT NULL,
    created_at timestamp
);

DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    url_id bigint,
    status_code int,
    h1 varchar(255) NOT NULL,
    title varchar(255),
    description text,
    created_at timestamp
);
