package flashcardsbackend.domain.questao.dto.gemini;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GeminiDTOResponse {
    private List<CandidateDTO> candidates;
    private PromptFeedback promptFeedback;
}

@Data
class PromptFeedback {
    List<Category> safetyRatings;
}

@Data
class MensagemDTOResponse {
    private List<MensagemDTOResponse> contents = new ArrayList<>();
    private PromptFeedback  promptFeedback;
}

@Data
class Category {
    private String category;
    private String probability;
}
