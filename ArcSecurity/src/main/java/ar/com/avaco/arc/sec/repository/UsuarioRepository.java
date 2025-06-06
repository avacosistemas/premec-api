package ar.com.avaco.arc.sec.repository;

import ar.com.avaco.arc.sec.domain.Usuario;

import java.util.List;

import ar.com.avaco.arc.core.component.bean.repository.NJRepository;

public interface UsuarioRepository extends NJRepository<Long, Usuario>, UsuarioRepositoryCustom {
	
	Usuario findByUsername(String username);

	Usuario findByEmail(String email);
	
	boolean isUserExistWithEmail(String email);

	List<Usuario> findByLegajoIn(List<String> legajos);

	Usuario findByLegajo(int legajo);
		
}