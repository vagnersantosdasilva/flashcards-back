package flashcardsbackend.domain.questao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DadosQuestaoUpdate(@NotNull
                                 Long id,
                                 @NotNull
                                 Long categoriaId,
                                 @NotBlank(message="Campo Pergunta não pode ser vazio")
                                 String pergunta,
                                 @NotBlank(message="Campo Resposta não pode ser vazio")
                                 String resposta,
                                 Boolean acerto,
                                 @NotNull
                                 UUID usuarioId
                                 ) {
}
