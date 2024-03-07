package flashcardsbackend.infra.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({EntityNotFoundException.class, NoHandlerFoundException.class})
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
                .body(new DadosErros("Credenciais inválidas. Verifique seu login e senha."));
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity recursoNaoEncontrado(ResourceNotFound ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new DadosErros(ex.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    @ResponseBody
    public ResponseEntity tokenException(TokenException ex){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new DadosErros(ex.getMessage()));
    }

    @ExceptionHandler(Validation.class)
    public ResponseEntity validationHtml(Validation ex){
        return ResponseEntity.badRequest().body(new DadosErros(ex.getMessage()));
    }

    @ExceptionHandler(SMTPException.class)
    public ResponseEntity erroSMTP(SMTPException e){
        return ResponseEntity.internalServerError().body(new DadosErros(e.getMessage()));
    }

    private record DadosErroValidacao(String field, String message) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record DadosErros(String message){}

}
