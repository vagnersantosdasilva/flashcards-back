package flashcardsbackend.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record DadosUsuarioResetPasswordDTO (
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
