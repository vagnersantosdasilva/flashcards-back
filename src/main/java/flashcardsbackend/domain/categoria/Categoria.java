package flashcardsbackend.domain.categoria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import flashcardsbackend.domain.questao.Questao;
import flashcardsbackend.domain.relatorios.TentativaEtapa;
import flashcardsbackend.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Table(name="categorias")
@Entity(name="Categoria")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String nome;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Questao> questoes;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.REMOVE)
    private List<TentativaEtapa> tentativasEtapas;

    @ManyToOne()
    private Usuario usuario;

}
