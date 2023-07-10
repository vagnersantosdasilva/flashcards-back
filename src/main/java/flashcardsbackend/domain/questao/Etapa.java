package flashcardsbackend.domain.questao;

public enum Etapa {

    ETAPA0("Etapa 0",0),
    ETAPA1("Etapa 1", 1),
    ETAPA2("Etapa 2", 4),
    ETAPA3("Etapa 3", 7),
    ETAPA4("Etapa 4", 14),
    ETAPA5("Etapa 5", 60),
    ETAPA6("Etapa 6", 180);

    private final String descricao;
    private final int duracaoDias;

    Etapa(String descricao, int duracaoDias) {
        this.descricao = descricao;
        this.duracaoDias = duracaoDias;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getDuracaoDias() {
        return duracaoDias;
    }
}
