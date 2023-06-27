CREATE TABLE categorias (
    id bigint PRIMARY KEY auto_increment,
    nome VARCHAR(255) UNIQUE,
    usuario_id BINARY(16),

    FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);