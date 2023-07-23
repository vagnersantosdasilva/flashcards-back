package flashcardsbackend.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


public record DadosUsuarioResetPasswordDTO (
        @NotNull
        UUID idUsuario,

        @NotNull
        @Email
        String username,

        @NotNull
        @Size(min = 6, max = 255)
        String password,

        @NotNull
        @NotBlank
        String confirmPassword){

        public boolean isPasswordsEquals(){
                return password.equals(confirmPassword);
        }
}
