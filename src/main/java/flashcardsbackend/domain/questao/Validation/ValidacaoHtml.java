package flashcardsbackend.domain.questao.Validation;

import org.springframework.stereotype.Component;

@Component
public interface ValidacaoHtml {
    void validar(String texto,String campo);
}
