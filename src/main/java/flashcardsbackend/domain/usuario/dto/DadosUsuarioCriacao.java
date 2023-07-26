package flashcardsbackend.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosUsuarioCriacao(
        @Email(message="Username deve ter formato válido de e-mail")
        String username,
        @NotBlank(message="Campo password não deve sem vazio")
        String password,

        @NotBlank(message="Campo confirmPassword não deve ser vazio")
        String confirmPassword
) {
}
