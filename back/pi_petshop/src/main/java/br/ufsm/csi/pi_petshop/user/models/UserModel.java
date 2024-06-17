package br.ufsm.csi.pi_petshop.user.models;

import br.ufsm.csi.pi_petshop.user.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuarios")
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em", nullable = false, updatable = false)
    private Date criadoEm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="atualizado_em", nullable = true)
    private Date atualizadoEm;

    public UserModel(String email, String senha, UserRole userRole) {
        this.email = email;
        this.senha = senha;
        this.userRole = userRole;
    }

    public UserModel(Long id, String email, UserRole userRole){
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.userRole == userRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_FUNCIONARIO"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getUsername() {
        return email;
    }

    public String getPassword() {
        return senha;
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
        return true;
    }
}
