package flashcardsbackend.infra.util.FileUtil;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public interface FileUtil {
    String getContent(MultipartFile file) throws IOException;
    String getContent(MultipartFile file, Integer start, Integer end) throws IOException;
    String getContent(MultipartFile file, Integer[] interval) throws IOException;
}
