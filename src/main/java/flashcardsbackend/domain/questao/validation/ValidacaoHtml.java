package flashcardsbackend.domain.questao.validation;

import org.springframework.stereotype.Component;

@Component
public interface ValidacaoHtml {
    void validar(String texto,String campo);
}
