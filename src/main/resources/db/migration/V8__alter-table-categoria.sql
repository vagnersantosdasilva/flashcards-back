-- ALTER TABLE categorias DROP INDEX nome;
ALTER TABLE categorias ADD CONSTRAINT unique_categoria_por_usuario UNIQUE (nome, usuario_id);
