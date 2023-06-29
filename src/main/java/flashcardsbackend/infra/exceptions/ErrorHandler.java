package flashcardsbackend.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(ValidPasswordException.class)
    public ResponseEntity tratarErroCampoConfirmPassword(ValidPasswordException ex){
        return ResponseEntity.badRequest().body(new DadosErroValidacao("confirmPassword", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity tratarUsuarioNaoEncontrado(UserNotFound ex){
        return ResponseEntity.badRequest().body(new DadosErroValidacao("username", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateUser.class)
    public ResponseEntity tratarUsuarioNaoEncontrado(DuplicateUser ex){
        return ResponseEntity.badRequest().body(new DadosErroValidacao("username", ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroLoginSenha(AuthenticationException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DadosErroValidacao("login","Credenciais inválidas. Verifique seu login e senha."));
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

}
