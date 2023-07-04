package flashcardsbackend.domain.questao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestaoRepository extends JpaRepository<Questao,Long> {
    Questao findByIdAndCategoriaIdAndCategoriaUsuarioId(Long idQuestao, Long idCategoria, UUID idUsuario);
    List<Questao> findAllByCategoriaIdAndCategoriaUsuarioId(Long idCategoria, UUID idUsuario);
    void deleteByIdAndCategoriaIdAndCategoriaUsuarioId(Long id, Long categoriaId, UUID usuarioId);
}
