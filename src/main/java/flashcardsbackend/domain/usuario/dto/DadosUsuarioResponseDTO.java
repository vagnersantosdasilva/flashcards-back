package flashcardsbackend.domain.usuario.dto;

import flashcardsbackend.domain.usuario.Usuario;

public record DadosUsuarioResponseDTO(String id, String username ) {

    public DadosUsuarioResponseDTO(Usuario usuario){
        this(usuario.getId().toString(),usuario.getUsername());
    }
}
