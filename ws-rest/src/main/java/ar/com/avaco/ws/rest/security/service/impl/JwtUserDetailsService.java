package ar.com.avaco.ws.rest.security.service.impl;
import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.service.UsuarioService;

@Service(value="jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {


    private UsuarioService usuarioService;

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        Usuario user = (Usuario) this.usuarioService.loadUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        return user;
    }
    
	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
}