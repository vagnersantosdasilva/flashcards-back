CREATE TABLE tentativas_etapa (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BINARY(16),
    categoria_id BIGINT,
    etapa varchar(8),
    acerto BOOLEAN,
    data_tentativa datetime DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);