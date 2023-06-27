package flashcardsbackend.domain.usuario;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity(name="Usuario")
@Table(name="usuarios")
@Data
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean isEnabled;

}
