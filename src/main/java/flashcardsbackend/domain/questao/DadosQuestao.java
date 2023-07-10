package flashcardsbackend.domain.questao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DadosQuestao(
        Long id,
        @NotNull
        Long categoriaId,

        @NotBlank
        String pergunta,
        @NotBlank
        String resposta,

        Boolean acerto,
        @NotNull
        UUID usuarioId

        ) {

    public DadosQuestao(Questao questao){
        this(questao.getId(),questao.getCategoria().getId(),questao.getPergunta(),questao.getResposta(),questao.getAcerto(),null);
    }
}
