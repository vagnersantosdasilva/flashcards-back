CREATE TABLE usuarios (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    isEnabled BOOLEAN NOT NULL
);