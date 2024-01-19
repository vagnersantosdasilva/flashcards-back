package flashcardsbackend.domain.categoria.utils;

import java.util.Comparator;

public class ContadorComparator implements Comparator<Contador> {
    @Override
    public int compare(Contador c1, Contador c2) {
        // Comparação com base na contagem (do menor para o maior)
        return Integer.compare(c1.getContagem(), c2.getContagem());
    }

}
