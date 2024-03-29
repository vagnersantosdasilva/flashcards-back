package flashcardsbackend.domain.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity(name="Usuario")
@Table(name="usuarios")
@Data
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false)
    protected UUID id;

    protected String username;

    @JsonIgnore
    protected String password;

    @Column(name = "enabled")
    protected Boolean enabled;

    @Column(name="social")
    protected Boolean social;

    @Column(name="id_social")
    protected String idSocial;

    @Column(name="provedor")
    protected String provedor;

    Usuario(){}

    public Usuario(String username, Boolean enabled, Boolean social, String provedor, String idSocial ){
        this.username = username;
        this.enabled = enabled;
        this.social = social;
        this.provedor = provedor;
        this.idSocial = idSocial;
    }

    public Usuario(String username, String password){
        this.username =username;
        this.password = password;
    }

    public Usuario(String username,String password, Boolean enabled){
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
