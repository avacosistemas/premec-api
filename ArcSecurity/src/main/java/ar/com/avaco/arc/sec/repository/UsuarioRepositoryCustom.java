package ar.com.avaco.arc.sec.repository;

import java.util.List;

import ar.com.avaco.arc.sec.domain.Usuario;

public interface UsuarioRepositoryCustom {

//	/**
//	 * Loads User by username
//	 * 
//	 * @param username
//	 *            the username to look for the user.
//	 * @return a User if found or null if not.
//	 */
//	Usuario getUserByUsername(String username);
	
	/**
	 * Loads User like username
	 * 
	 * @param username
	 *            the username to look for the user.
	 * @return a User if found or null if not.
	 */
	List<Usuario> getExternalUsersLike(String userLess,String userlike);
	
	/**
	 * Gets the user by email
	 * @param username
	 * @return
	 */
//	Usuario getUserByEmail(String email);

	List<Usuario> listUsuariosParaImpersonar(Usuario usuario);
	
	List<Usuario> getAllOnlyIdUsernameAndName();
}