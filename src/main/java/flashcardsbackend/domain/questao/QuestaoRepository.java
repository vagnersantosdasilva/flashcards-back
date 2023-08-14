package flashcardsbackend.domain.questao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface QuestaoRepository extends JpaRepository<Questao,Long> {
    Questao findByIdAndCategoriaIdAndCategoriaUsuarioId(Long idQuestao, Long idCategoria, UUID idUsuario);

    List<Questao> findAllByCategoriaIdAndCategoriaUsuarioId(Long idCategoria, UUID idUsuario);

    void deleteByIdAndCategoriaIdAndCategoriaUsuarioId(Long id, Long categoriaId, UUID usuarioId);

    @Query("SELECT COUNT(q) FROM Questao q WHERE q.etapa = 'ETAPA0' AND q.categoria.id = :idCategoria AND q.categoria.usuario.id = :idUsuario")
    Long countByEtapa0AndCategoriaIdAndCategoriaUsuarioId(Long idCategoria, UUID idUsuario);

    @Query(nativeQuery = true , value = "SELECT\n" +
            "    CASE\n" +
            "        WHEN etapa = 'ETAPA0' THEN DATE_ADD(date(data_criacao), INTERVAL 0 DAY)\n" +
            "        WHEN etapa = 'ETAPA1' THEN DATE_ADD(date(data_criacao), INTERVAL 1 DAY)\n" +
            "        WHEN etapa = 'ETAPA2' THEN DATE_ADD(date(data_criacao), INTERVAL 4 DAY)\n" +
            "        WHEN etapa = 'ETAPA3' THEN DATE_ADD(date(data_criacao), INTERVAL 7 DAY)\n" +
            "        WHEN etapa = 'ETAPA4' THEN DATE_ADD(date(data_criacao), INTERVAL 14 DAY)\n" +
            "        WHEN etapa = 'ETAPA5' THEN DATE_ADD(date(data_criacao), INTERVAL 60 DAY)\n" +
            "        WHEN etapa = 'ETAPA6' THEN DATE_ADD(date(data_criacao), INTERVAL 180 DAY)\n" +
            "    END AS nova_data\n" +
            "FROM flashcards.questoes\n" +
            "WHERE categoria_id = ? \n" +
            "    AND\n" +
            "    CASE\n" +
            "        WHEN etapa = 'ETAPA0' THEN DATE_ADD(date(data_criacao), INTERVAL 0 DAY)\n" +
            "        WHEN etapa = 'ETAPA1' THEN DATE_ADD(date(data_criacao), INTERVAL 1 DAY)\n" +
            "        WHEN etapa = 'ETAPA2' THEN DATE_ADD(date(data_criacao), INTERVAL 4 DAY)\n" +
            "        WHEN etapa = 'ETAPA3' THEN DATE_ADD(date(data_criacao), INTERVAL 7 DAY)\n" +
            "        WHEN etapa = 'ETAPA4' THEN DATE_ADD(date(data_criacao), INTERVAL 14 DAY)\n" +
            "        WHEN etapa = 'ETAPA5' THEN DATE_ADD(date(data_criacao), INTERVAL 60 DAY)\n" +
            "        WHEN etapa = 'ETAPA6' THEN DATE_ADD(date(data_criacao), INTERVAL 180 DAY)\n" +
            "    END >= CURDATE()" +
            "    GROUP BY nova_data   ")
    List<Object> getDataRevisaoPorCategoria(Long idCategoria);
}
