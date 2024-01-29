package flashcardsbackend.domain.relatorios;

import java.math.BigDecimal;

public interface AcertoEtapaDTO extends Comparable<AcertoEtapaDTO>{
    String getEtapa();
    Integer getTentativas();
    Integer getAcertos();
    BigDecimal getAproveitamento();

    @Override
    default int compareTo(AcertoEtapaDTO other) {
        // Comparação com base na propriedade desejada (por exemplo, etapa)
        return this.getEtapa().compareTo(other.getEtapa());
    }
}
