package flashcardsbackend.domain.questao;

import flashcardsbackend.domain.categoria.Categoria;
import flashcardsbackend.domain.relatorios.TentativaEtapa;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Table(name="questoes")
@Entity(name="Questao")
@Data
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Categoria categoria;

    private String pergunta;

    private String resposta;

    private Boolean acerto;

    @Column(name="data_criacao")
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private Etapa etapa;

    public Questao(Long id, String pergunta, String resposta, Boolean acerto, Etapa etapa){
        this.id = id;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.acerto = acerto;
        this.etapa = etapa;
    }
    public Questao(){}

}
