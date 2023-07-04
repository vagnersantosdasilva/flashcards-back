package flashcardsbackend.domain.questao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DadosQuestaoUpdate(@NotNull
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
}
