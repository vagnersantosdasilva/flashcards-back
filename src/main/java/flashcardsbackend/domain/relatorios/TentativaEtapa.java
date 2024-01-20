package flashcardsbackend.domain.relatorios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import flashcardsbackend.domain.categoria.Categoria;
import flashcardsbackend.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="tentativas_etapa")
public class TentativaEtapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="etapa")
    private String etapa;
    @JsonIgnore
    @ManyToOne()
    private Usuario usuario;

    @JsonIgnore
    @ManyToOne()
    private Categoria categoria;

    @Column(name="acerto")
    private Boolean acerto;

    @Column(name="data_tentativa")
    private LocalDateTime dataTentativa;

}
