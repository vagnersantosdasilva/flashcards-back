package flashcardsbackend.domain.usuario;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Entity(name="Usuario")
@Table(name="usuarios")
@Data
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
    private String username;
    private String password;

    @Column(name = "enabled")
    private Boolean enabled;

    Usuario(){}

    Usuario(String username,String password){
        this.username =username;
        this.password = password;
    }

    Usuario(String username,String password, Boolean enabled){
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

}
