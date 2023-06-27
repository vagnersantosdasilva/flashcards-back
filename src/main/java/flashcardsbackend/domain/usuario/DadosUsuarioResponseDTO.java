package flashcardsbackend.domain.usuario;

public record DadosUsuarioResponseDTO(String id, String username ) {

    DadosUsuarioResponseDTO(Usuario usuario){
        this(usuario.getId().toString(),usuario.getUsername());
    }
}
