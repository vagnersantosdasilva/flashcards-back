package flashcardsbackend.domain.relatorios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TentativaEtapaService {
    private TentativaEtapaRepository tentativaEtapaRepository;
    @Autowired
    TentativaEtapaService(TentativaEtapaRepository tentativaEtapaRepository){
        this.tentativaEtapaRepository = tentativaEtapaRepository;
    }
    public List<AcertoEtapaDTO> getListAcertoEtapaPorCategoria(UUID idUsuario, Long idCategoria){
        return tentativaEtapaRepository.obterAcertosPorCategoriaEEtapa(idUsuario,idCategoria);
    }
}
