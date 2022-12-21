package ar.com.avaco.arc.sec.repository;

import java.util.List;

import ar.com.avaco.arc.sec.domain.Rol;

public interface RolRepositoryCustom {	
	List<Rol> rolPorNombre(String query);
	
	Rol getRolPorCodigo(String codigo);
	
	Rol getRolPorNombre(String nombre);
	
	List<Rol> getRolesPorCodigos(List<String> codigos);
	
	List<Rol> rolesNoTransportista();
	
	boolean isRolAsignadoAcceso(Rol rol);
}