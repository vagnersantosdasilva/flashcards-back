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

}
