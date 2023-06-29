package flashcardsbackend.domain.usuario.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosUsuarioDTO(
        @Email
        String username,
        @NotBlank
        String password ) {
}
