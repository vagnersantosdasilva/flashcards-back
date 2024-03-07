package flashcardsbackend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.infra.exceptions.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.token.issuer}")
    private String issuer;
    public String gerarToken(Usuario usuario){
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withClaim("idUser",usuario.getId().toString())
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new TokenException("Erro ao gerar token jwt : " + exception.getMessage());
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new TokenException("Acesso expirado! Repita o processo de autenticação para voltar a ter acesso.");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"));
    }

}
