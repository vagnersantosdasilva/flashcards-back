package flashcardsbackend.infra.util.FileUtil;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileUtilImplementacao implements FileUtil{

    @Override
    public String getContent(MultipartFile file) throws IOException {
        String content = "";
        if (file!=null){
            String fileType = file.getContentType();
            if (fileType.equals("text/plain")) {
                InputStream inputStream = file.getInputStream();
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
                content = scanner.useDelimiter("\\A").next();
                return content;
            }
            if (fileType.equals("application/pdf")) {
                PDDocument document = PDDocument.load(file.getInputStream());
                PDFTextStripper stripper = new PDFTextStripper();
                content = stripper.getText(document);
                return content;
            }
        }
        return content;
    }

    @Override
    public String getContent(MultipartFile file, Integer start, Integer end) throws IOException {
        String content = "";
        if (file!=null){
            String fileType = file.getContentType();
            if (fileType.equals("application/pdf")) {
                PDDocument document = PDDocument.load(file.getInputStream());
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setStartPage(start);
                stripper.setEndPage(end);
                content = stripper.getText(document);

            }
            return content;
        }
        return content;
    }

    @Override
    public String getContent(MultipartFile file, Integer[] pages) throws IOException {
        String content= "";
        if (file!=null){
            String fileType = file.getContentType();
            if (fileType.equals("application/pdf")) {
                PDDocument document = PDDocument.load(file.getInputStream());
                PDFTextStripper stripper = new PDFTextStripper();
                for (Integer page : pages){
                    stripper.setStartPage(page);
                    stripper.setEndPage(page);
                    String pageContent = stripper.getText(document);
                    content += pageContent;
                }
            }
            return content;
        }
        return "";
    }
}
