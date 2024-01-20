ALTER TABLE questoes
ADD COLUMN tentativa_id BIGINT,
ADD FOREIGN KEY (tentativa_id) REFERENCES tentativas_etapa (id);