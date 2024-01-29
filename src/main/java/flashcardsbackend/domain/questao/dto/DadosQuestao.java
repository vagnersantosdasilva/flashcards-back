package flashcardsbackend.domain.questao.dto;

import flashcardsbackend.domain.questao.Questao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DadosQuestao(
        Long id,
        @NotNull
        Long categoriaId,

        @NotBlank ( message = "Campo Pergunta não pode ser vazio")
        String pergunta,
        @NotBlank ( message = "Campo Resposta não pode ser vazio")
        String resposta,

        Boolean acerto,
        @NotNull
        UUID usuarioId

        ) {

    public DadosQuestao(Questao questao){
        this(questao.getId(),questao.getCategoria().getId(),questao.getPergunta(),questao.getResposta(),questao.getAcerto(),null);
    }
}
