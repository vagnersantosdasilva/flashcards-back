CREATE TABLE questoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    categoria_id BIGINT,
    etapa varchar(8),
    pergunta TEXT,
    resposta TEXT,
    acerto BOOLEAN,
    data_criacao datetime,
    FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);
