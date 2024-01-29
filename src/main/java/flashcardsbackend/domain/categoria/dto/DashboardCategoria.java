package flashcardsbackend.domain.categoria.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DashboardCategoria {

        private Long idCategoria;
        private String nomeCategoria;
        private Integer etapa0 =0;
        private Integer etapa1 =0;
        private Integer etapa2 =0;
        private Integer etapa3 =0;
        private Integer etapa4 =0;
        private Integer etapa5 =0;
        private Integer etapa6 =0;
        private Integer totalQuestoes = 0;
        private String nivel = "INICIANTE";

        public DashboardCategoria(Long idCategoria, String nomeCategoria, Integer etapa1, Integer etapa2, Integer etapa3, Integer etapa4, Integer etapa5, Integer etapa6) {
                this.idCategoria = idCategoria;
                this.nomeCategoria = nomeCategoria;
                this.etapa1 = etapa1;
                this.etapa2 = etapa2;
                this.etapa3 = etapa3;
                this.etapa4 = etapa4;
                this.etapa5 = etapa5;
                this.etapa6 = etapa6;
        }
}
