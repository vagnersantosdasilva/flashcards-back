package flashcardsbackend.domain.categoria;

import flashcardsbackend.domain.categoria.dto.ContagemEtapaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
    List<Categoria> findByUsuarioId(UUID idUsuario);

    Optional<Categoria> findByNomeAndUsuarioId(String nome, UUID usuarioId);
    @Query(nativeQuery = true,value="SELECT etapa, count(etapa) FROM flashcards.questoes where categoria_id = :idCategoria  group by etapa")
    List<Object> getCountEtapaByCategory(Long idCategoria);

    @Query(nativeQuery = true,value = "SELECT c.id as id, c.nome as nome, q.etapa as etapa, COUNT(q.etapa) AS quantidade\n" +
            "FROM flashcards.usuarios u\n" +
            "JOIN flashcards.categorias c ON BIN_TO_UUID(u.id) = BIN_TO_UUID(c.usuario_id) \n" +
            "JOIN flashcards.questoes q ON q.categoria_id = c.id\n" +
            "WHERE BIN_TO_UUID(u.id) = BIN_TO_UUID(:idUsuario)\n" +
            "GROUP BY c.id, c.nome, q.etapa;")
    List<ContagemEtapaDTO> getCountEtapaByCategoria(UUID idUsuario);
}
