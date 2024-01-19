package flashcardsbackend.domain.categoria.utils;

import lombok.Data;

@Data
public class Contador {
    private String descricao;
    private int contagem;

    public Contador(String descricao, int contagem){
        this.descricao = descricao;
        this.contagem = contagem;
    }
}
