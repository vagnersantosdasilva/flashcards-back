package flashcardsbackend.infra.security;

import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.domain.usuario.UsuarioRepository;

import flashcardsbackend.domain.usuario.UsuarioService;
import flashcardsbackend.domain.usuario.dto.DadosUsuarioSocialCriacao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Lazy
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Value("${frontend.uri}")
    private String frontend;
    UsuarioService usuarioService;
    TokenService tokenService;
    @Autowired
    public OAuth2LoginSuccessHandler(UsuarioService usuarioService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    String token ="";
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        //DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
            token = getToken(oAuth2User.getAttribute("sub"),
                    oAuth2User.getAttribute("name"),
                    oAuth2User.getAttribute("email"),
                    oAuth2User.getAttribute("email"),
                    "google"
            );
        }
        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
            token = getToken(
                    oAuth2User.getAttribute("id").toString(),
                    oAuth2User.getAttribute("name"),
                    oAuth2User.getAttribute("email"),
                    oAuth2User.getAttribute("login"),
                    "github"
            );
        }
        if ("facebook".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
            token = getToken(
                    oAuth2User.getAttribute("id"),
                    oAuth2User.getAttribute("name"),
                    oAuth2User.getAttribute("email"),
                    oAuth2User.getAttribute("login"),
                    "facebook"
            );

        }
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontend+"/?token="+token);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String getToken(String id, String name, String email, String username, String provedor) throws GeneralSecurityException, IOException {
        if (id!=null && provedor=="google") {
            return usuarioService.criarUsuarioSocial(new DadosUsuarioSocialCriacao(username, provedor, id)).token();
        }
        if (id!=null && provedor =="facebook"){
            String user = name.replace(" ","_")+"-"+id;
            return usuarioService.criarUsuarioSocial(new DadosUsuarioSocialCriacao(user, provedor, id)).token();
        }

        if (id!=null && provedor =="github"){
            String user = username.replace(" ","_")+"_"+id;
            return usuarioService.criarUsuarioSocial(new DadosUsuarioSocialCriacao(user, provedor, id)).token();
        }
        return null;
    }
}
