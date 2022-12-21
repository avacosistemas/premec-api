package ar.com.avaco.arc.sec.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.commons.exception.ErrorValidationException;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations={"classpath:arc/sec/spring/arcSecContext.xml"})
public class UsuarioTest {

	@Resource
	private UsuarioService usuarioService;
	
	@Test
	public void newUserTest() {
		
		Usuario usuario = nuevoUsuario();

		Usuario savedUsuario = usuarioService.save(usuario);
		
		Assert.assertNotNull(savedUsuario.getId());
		Assert.assertFalse(savedUsuario.isBloqueado());
		Assert.assertTrue(savedUsuario.isRequiereCambioPassword());

		Assert.assertEquals(savedUsuario.getApellido(), usuario.getApellido());
		Assert.assertEquals(savedUsuario.getEmail(), usuario.getEmail());
		Assert.assertEquals(savedUsuario.getApellido(), usuario.getApellido());
		Assert.assertEquals(savedUsuario.getUsername(), usuario.getUsername());
		Assert.assertEquals(savedUsuario.getIntentosFallidosLogin(), new Integer(0));
		
	}
	
	@Test(expected = ErrorValidationException.class)
	public void usuarioRepetidoTest() {
		Usuario usuario = nuevoUsuario("repetido");
		usuarioService.save(usuario);
		Usuario repetido = nuevoUsuario("repetido");
		usuarioService.save(repetido);
	}

	@Test
	public void loadUserByUsernameTest() {
		Usuario usuario = nuevoUsuario("beto");
		usuarioService.save(usuario);
		Usuario byUsername = (Usuario) usuarioService.loadUserByUsername(usuario.getUsername());
		Assert.assertNotNull(byUsername);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void failLoadUserByUsernameTest() {
		usuarioService.loadUserByUsername("jacinto");
	}
	
	@Test
	public void usuariosImpersonadosTest() {
		Usuario principal = nuevoUsuario("principal");
		Usuario impersonado1 = nuevoUsuario("impersonado1");
		Usuario impersonado2 = nuevoUsuario("impersonado2");
		
		principal = usuarioService.save(principal);
		impersonado1 = usuarioService.save(impersonado1);
		impersonado2 = usuarioService.save(impersonado2);
		
		List<Usuario> impersonables = new ArrayList<Usuario>();
		impersonables.add(impersonado1);
		impersonables.add(impersonado2);
		
		usuarioService.update(principal, impersonables);
		
		List<Usuario> listUsuariosParaImpersonar = usuarioService.listUsuariosParaImpersonar(principal);
		
		Assert.assertEquals(impersonables.size(), listUsuariosParaImpersonar.size());
		Assert.assertTrue(impersonables.containsAll(listUsuariosParaImpersonar));
		
	}
	
	@Test
	public void usuarioExiste() {
		Usuario principal = nuevoUsuario("existente");
		Assert.assertFalse(usuarioService.isUserExists(principal.getUsername()));
		usuarioService.save(principal);
		Assert.assertTrue(usuarioService.isUserExists(principal.getUsername()));
	}
	
	private Usuario nuevoUsuario(String prefix) {
		Usuario usuario = new Usuario();
		usuario.setApellido(prefix + "apellido");
		usuario.setEmail(prefix + "email@email.com");
		usuario.setInterno(true);
		usuario.setNombre(prefix + "nombre");
		usuario.setUsername(prefix + "username");
		return usuario;
	}

	private Usuario nuevoUsuario() {
		return nuevoUsuario("");
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
}
