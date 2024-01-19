package flashcardsbackend;

import flashcardsbackend.domain.categoria.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FlashcardsBackendApplicationTests {
    @Autowired
    CategoriaRepository repository;
    @Test
    void testCategoriaServiceContagem(){

    }
}
