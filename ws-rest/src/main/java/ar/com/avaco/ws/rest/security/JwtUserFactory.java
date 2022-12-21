package ar.com.avaco.ws.rest.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.model.JwtUser;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Usuario user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getNombre(),
                user.getApellido(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getAuthorities()),
                user.getFechaAltaPassword() != null,
                user.getFechaAltaPassword()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
    }
}