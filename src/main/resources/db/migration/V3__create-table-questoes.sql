CREATE TABLE questoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    categoria_id BIGINT,
    pergunta VARCHAR(255),
    resposta VARCHAR(255),
    acerto BOOLEAN,

    FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);