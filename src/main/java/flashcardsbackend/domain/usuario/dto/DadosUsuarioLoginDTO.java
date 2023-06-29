package flashcardsbackend.domain.usuario.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosUsuarioLoginDTO(
        @Email
        String username,
        @NotBlank
        String password ) {
}
