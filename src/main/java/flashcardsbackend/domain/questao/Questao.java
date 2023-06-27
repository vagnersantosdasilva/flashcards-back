package flashcardsbackend.domain.questao;

import flashcardsbackend.domain.categoria.Categoria;
import jakarta.persistence.*;
import lombok.Data;

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
}
