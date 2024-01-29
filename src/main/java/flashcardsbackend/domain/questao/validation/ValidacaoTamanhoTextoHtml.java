package flashcardsbackend.domain.questao.validation;

import flashcardsbackend.infra.exceptions.Validation;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidacaoTamanhoTextoHtml implements ValidacaoHtml{
    @Override
    public void validar(String texto,String campo) {
        Pattern pattern = Pattern.compile("<img\\s+src=('|\")data:image/[^;]+;base64,[^\"']+\\1\\s*/?>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(texto);

        // Remova ou ignore as imagens base64
        String textWithoutImages = matcher.replaceAll("");

        // Verifique o tamanho do texto resultante
        int textLength = textWithoutImages.replaceAll("<[^>]+>", "").length();
        if (textLength>300 ) throw new Validation("Limite de caracteres atingido em campo "+campo);

    }
}
