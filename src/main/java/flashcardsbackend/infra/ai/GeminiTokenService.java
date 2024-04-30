package flashcardsbackend.infra.ai;

import com.google.gson.Gson;
import flashcardsbackend.domain.questao.dto.DadosQuestao;
import flashcardsbackend.domain.questao.dto.gemini.*;
import flashcardsbackend.infra.util.FileUtil.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GeminiTokenService {
    @Value("${gemini.key}")
    private String API_KEY;
    @Value("${gemini.url}")
    private String URL;

    @Autowired
    private FileUtil fileUtil;

    public List<DadosQuestao> generateContent(UUID idUsuario, Long idCategoria, String lingua, MultipartFile file, String paginas) throws IOException {
        String texto = getContent(file, paginas);
        String cleanedString = prepararTexto(texto,lingua);
        PartDTO partDTO = new PartDTO();
        partDTO.setText(cleanedString);
        GeminiDTO geminiDTO = new GeminiDTO();
        MensagemDTO mensagemDTO = new MensagemDTO();
        mensagemDTO.getParts().add(partDTO);
        geminiDTO.getContents().add(mensagemDTO);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<GeminiDTO> requestEntity =criarHttpEntity(geminiDTO);
        ResponseEntity<GeminiDTOResponse> responseEntity = restTemplate.postForEntity(URL, requestEntity, GeminiDTOResponse.class);
        List<DadosQuestao> listaQuestao = obterListaQuestao(idUsuario, idCategoria, responseEntity.getBody());
        return listaQuestao;
    }
    public String generateContent(String lingua , String text) {
            String prompt = """
                    Gere uma lista de perguntas e respostas curtas a partir dos principais tópicos do texto a seguir.
                    Importante! A pergunta deve ser iniciada pela palavra 'Pergunta:' e a resposta deve ser iniciada pela palavra 'Resposta:' """;
            String textoCompleto = lingua+":"+prompt+":"+text;
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> requestEntity =criarHttpEntity(textoCompleto);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, requestEntity, String.class);
            return responseEntity.getBody();
    }

    private HttpEntity<String> criarHttpEntity(String textoCompleto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", API_KEY);
        String texto = textoCompleto.replace("\\","&quot;").replace("<EOF>","");
        String requestBody = "{\"contents\":[{\"role\":\"user\",\"parts\":[{\"text\":\"" + texto + "\"}]}]}";
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);
        return requestEntity;
    }

    private HttpEntity<GeminiDTO> criarHttpEntity(GeminiDTO dto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", API_KEY);
        HttpEntity<GeminiDTO> requestEntity = new HttpEntity<>(dto, headers);
        return requestEntity;
    }

    public List<DadosQuestao> extrairPerguntasRespostas(UUID idUsuario, Long idCategoria, String texto){
        String regex = "Pergunta:";
        String[] vetor1 = texto.split(regex);
        List<DadosQuestao> questoes = new ArrayList<>();
        for (String s1: vetor1){
            String [] vetor2 = s1.split("Resposta:");
            if ( vetor2!=null && vetor2.length>1) {
                String p = vetor2[0].replace("**","").replace("\\n","").trim();
                String r = vetor2[1].replace("**","").replace("\\n","").trim();
                System.out.println(p+":"+r);
                DadosQuestao quetao = new DadosQuestao(null,idCategoria,p,r,null, idUsuario);
                questoes.add(quetao);
            }
        }
        return questoes;
    }

    private String prepararTexto(String texto, String lingua){
        String prompt = lingua+":Gere uma lista de perguntas e respostas curtas a partir dos principais tópicos do texto a seguir. Importante! A pergunta deve ser iniciada pela palavra 'Pergunta:' e a resposta deve ser iniciada pela palavra 'Resposta:'";
        String regex = "[\\r\\n\\t\\u0000]"; // Remove quebras de linha, tabulações e caracteres nulos
        String cleanedString = texto.replaceAll(regex, "")
                .replace("\\","")
                .replace("<","'<")
                .replace(">",">'")
                .replace("/","-")
                .replace("\\","")
                .replace("\"","")
                .replace("  ","");

        return prompt+cleanedString;
    }

    private String getContent(MultipartFile arquivo, String paginas) throws IOException {
        if (paginas !=null && paginas.indexOf("-")>0) {
            String [] s = paginas.split("-");
            int[] intensIntervalo = Arrays.stream(s)
                    .mapToInt(Integer::parseInt)
                    .toArray();
            return fileUtil.getContent(arquivo, intensIntervalo[0], intensIntervalo[1]);
        }
        if (paginas != null && paginas.indexOf(";")>0) {
            String [] s = paginas.split(";");
            Integer[] integerObjects = Arrays.stream(s)
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);
            return fileUtil.getContent(arquivo, integerObjects);
        }
        return fileUtil.getContent(arquivo);
    }

    private List<DadosQuestao> obterListaQuestao(UUID idUsuario, Long idCategoria, GeminiDTOResponse response){
        CandidateDTO c  = response.getCandidates().get(0);
        List<PartDTO> parts = c.getContent().getParts();
        return extrairPerguntasRespostas(idUsuario, idCategoria,parts.get(0).getText());
    }

}
