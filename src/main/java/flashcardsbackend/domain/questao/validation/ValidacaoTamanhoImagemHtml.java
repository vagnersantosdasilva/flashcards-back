package flashcardsbackend.domain.questao.validation;

import flashcardsbackend.infra.exceptions.Validation;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class ValidacaoTamanhoImagemHtml implements ValidacaoHtml{

    @Override
    public void validar(String texto,String campo) {
        Pattern pattern = Pattern.compile("data:image/[^;]+;base64,([^\"]+)");
        Matcher matcher = pattern.matcher(texto);

        while(matcher.find()){
            String base64Data = matcher.group(1);
            byte[] imageData = Base64.getDecoder().decode(base64Data);
            double fileSizeInKB = imageData.length / 1024.0;
            System.out.println("Tamanho da imagem em KB:"+fileSizeInKB);
            if (fileSizeInKB> 20) throw new Validation("Tamanho de imagem excede limite de 20KB em campo "+campo);
        }
    }
}
