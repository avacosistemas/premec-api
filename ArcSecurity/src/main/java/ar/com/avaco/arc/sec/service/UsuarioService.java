package ar.com.avaco.arc.sec.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ar.com.avaco.arc.core.component.bean.service.NJService;
import ar.com.avaco.arc.sec.domain.Usuario;

/**
 * the user service.
 * 
 * @author aogonzalez
 */
public interface UsuarioService extends NJService<Long, Usuario> {

	/**
	 * Updates the password of the user encrypting it.
	 * @param user The user to be used to set the new password.
	 * @param newPassword the new password with no encryption.
	 * @param user the user to update the password.
	 */
	void updatePassword(Usuario user, String password,String newPassword);

	/**
	 * Checks if the user exists.
	 * @param username
	 * @return
	 */
	boolean isUserExists(String username);
	
	void sendMissingPassword(String username);
	
	void sendMissingPasswordById(Long id);

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	List<Usuario> getExternalUsersLike(String userLess,String userLike);
	
	void generateNewPassword(Usuario user);
	
	Usuario getUserWithEmails(Usuario user);

	List<Usuario> listUsuariosParaImpersonar(Usuario usuario);

	void update(Usuario usuario, List<Usuario> impersonables);
	
	Usuario findById(Long id);
	
	boolean isUserExistWithEmail(String email);
	
	Usuario findByUsername(String username);
	
}

