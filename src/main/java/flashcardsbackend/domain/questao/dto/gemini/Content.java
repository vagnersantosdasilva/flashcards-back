package flashcardsbackend.domain.questao.dto.gemini;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Content{
    private List<PartDTO> parts = new ArrayList<>();
    private String role;
}