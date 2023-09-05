CREATE TABLE categorias (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255),
    usuario_id BINARY(16),

    CONSTRAINT fk_categoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT unique_categoria_por_usuario UNIQUE (nome, usuario_id)
);
