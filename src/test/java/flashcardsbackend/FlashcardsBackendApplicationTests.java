package flashcardsbackend;

import org.junit.jupiter.api.Test;


class FlashcardsBackendApplicationTests {
    @Test
    void testExtracao() {
        String s ="**Pergunta 1:** Para que a JPA foi criada?\n**Resposta:** Para resolver problemas de acesso a banco de dados.\n\n**Pergunta 2:** Qual a tecnologia padrão do Java para acessoa banco de dados relacionais?\n**Resposta:** JDBC\n\n**Pergunta 3:** Qual a função do JDBC?\n**Resposta:** Fornecer uma camada de abstração para acesso a bancos de dados, simplificando o processo.\n\n**Pergunta 4:** O que é necessário para acessar um banco de dados usando JDBC?\n**Resposta:** Um driver JAR com as classes do banco de dados.\n\n**Pergunta 5:** Qual o benefício de usar JDBC?\n**Resposta:** Facilidade na troca de bancos de dados, com impacto mínimo no código.\n\n**Pergunta 6:** Qual padrão de projeto é frequentemente usado para organizar o código JDBC?\n**Resposta:** DAO".replace("\n","");
        String[] vetor1 = s.split("\\*\\*Pergunta \\d:\\*\\*");
        for (String s1: vetor1){
            String [] vetor2 = s1.split("Resposta:");
            System.out.println(vetor2.length);
            if ( vetor2!=null && vetor2.length>1) {
                String p = vetor2[0].replace("**","");
                String r = vetor2[1].replace("**","");
            }
        }


    }
}
