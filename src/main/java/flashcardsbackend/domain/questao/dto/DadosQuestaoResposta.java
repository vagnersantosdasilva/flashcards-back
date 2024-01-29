package flashcardsbackend.domain.questao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DadosQuestaoResposta(@NotNull
                                   Long id,
                                   @NotNull
                                   Long categoriaId,

                                   Boolean acerto,
                                   @NotNull
                                   UUID usuarioId) {
}
