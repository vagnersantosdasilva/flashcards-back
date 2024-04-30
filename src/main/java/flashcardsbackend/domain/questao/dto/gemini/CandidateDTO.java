package flashcardsbackend.domain.questao.dto.gemini;

import lombok.Data;

import java.util.List;

@Data
public class CandidateDTO {
    private Content content;
    private String finishReason;
    private Long index;
    private List<Category> safetyRatings;
}