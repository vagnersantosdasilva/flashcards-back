package flashcardsbackend.domain.categoria.dto;

import flashcardsbackend.domain.categoria.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DadosCategoria(
      Long id,
      @NotNull(message="ID de usuário não pode ser nulo")
      UUID usuarioId,
      @NotBlank(message = "Nome de categoria não pode ser vazio")
      String nome

) {
    public DadosCategoria(Categoria categoria){
        this(
                categoria.getId(),
                categoria.getUsuario().getId(),
                categoria.getNome());
    }
}
