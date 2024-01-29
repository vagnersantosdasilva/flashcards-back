package flashcardsbackend.domain.relatorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TentativaEtapaRepository extends JpaRepository<TentativaEtapa,Long> {
    @Query(nativeQuery = true,value="SELECT etapa, count(*) as tentativas, sum(acerto) as acertos, \n" +
            "ROUND(SUM(acerto) * 100 / COUNT(*)) as aproveitamento \n" +
            "FROM flashcards.tentativas_etapa               \n" +
            "WHERE  usuario_id = :idUsuario and             \n" +
            "categoria_id = :idCategoria                    \n" +
            "GROUP BY etapa;")
    List<AcertoEtapaDTO> obterAcertosPorCategoriaEEtapa(UUID idUsuario, Long idCategoria);

    List<TentativaEtapa> findByCategoriaId(Long idCategoria);
}
