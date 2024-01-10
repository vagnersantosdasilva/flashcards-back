package flashcardsbackend.domain.usuario.dto;

import flashcardsbackend.domain.usuario.Usuario;

public record DadosUsuarioSocialResponseDTO(String id , String username, String token){
    public DadosUsuarioSocialResponseDTO(Usuario usuario,String token){
        this(usuario.getId().toString(),usuario.getUsername(),token);
    }
}
