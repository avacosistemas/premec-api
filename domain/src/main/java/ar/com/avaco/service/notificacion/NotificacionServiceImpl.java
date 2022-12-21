package ar.com.avaco.service.notificacion;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.entities.cliente.Cliente;
import ar.com.avaco.utils.DateUtils;

@Service("notificacionService")
public class NotificacionServiceImpl implements NotificacionService {

	private static final String CLASSPATH_RESOURCE_LOADER_CLASS = "classpath.resource.loader.class";
	private static final String CLASSPATH = "classpath";
	private static final String ISO_8859_1 = "ISO-8859-1";

	private VelocityEngine ve;

	private MailSenderSMTPService mailSenderSMTPService;

	@Value("none@gmail.com")
	private String from;

	@Value("Restablecimiento de contraseña")
	private String subjectResetoPasswordCliente;

	@Value("template/reseteo-password-cliente.html")
	private String bodyResetoPasswordCliente;

	@Value("Bienvenido")
	private String subjectRegistroClienteNuevoPassword;

	@Value("template/registro-cliente-nuevo-password.html")
	private String bodyRegistroClienteNuevoPassword;	
	
	@Value("Usuario Habiliatdo!")
	private String subjectHabilitacionExistosa;
	
	@Value("template/notificacion-habilitacion-existosa.html")
	private String bodyHabilitacionExistosa;
	
	@Value("template/header-general.html")
	private String headerGeneral;

	@Value("template/footer-general.html")
	private String footerGeneral;
	

	@Value("avacosistemas@gmail.com")
	private String supportMail;

	@Value("none@gmail.com")
	private String notificacionInterna;
	
	public NotificacionServiceImpl() {
		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, CLASSPATH);
		ve.setProperty(CLASSPATH_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
		ve.init();
	}

	public void notificarInicioServer() {
		mailSenderSMTPService.sendMail(from, supportMail, "Inicio Server Tomcat",
				"Se inicio el server. " + DateUtils.toString(new Date(), DateUtils.PATTERN_FULL_24_HS), null);
		
	}

	private String getHeader() {
		VelocityContext context = new VelocityContext();
		Template template = ve.getTemplate(headerGeneral, ISO_8859_1);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

	private String getFooter() {
		VelocityContext context = new VelocityContext();
		Template template = ve.getTemplate(footerGeneral, ISO_8859_1);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

	protected String getBody(Map<String, String> parametros, String pathBody) {
		VelocityContext context = new VelocityContext();
		parametros.forEach((clave, valor) -> context.put(clave, valor));
		Template template = ve.getTemplate(pathBody, ISO_8859_1);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		StringBuilder sb = new StringBuilder();
		sb.append(getHeader());
		sb.append(writer.toString());
		sb.append(getFooter());
		return sb.toString();
	}

	@Async
	@Override
	public void notificarResetoPassword(Cliente cliente, String tmppass) {
		Map<String, String> params = new HashMap<String, String>();
		//params.put("cliente", cliente.getNombreApellido());
		//params.put("usuario", cliente.getUsername());
		params.put("password", tmppass);
		mailSenderSMTPService.sendMail(from, cliente.getEmail(), subjectResetoPasswordCliente,
				getBody(params, bodyResetoPasswordCliente), null);
	}

	@Async
	@Override
	public void notificarRegistroClienteNuevoPassword(Cliente cliente, String tmpass) {
		Map<String, String> params = new HashMap<String, String>();
		//params.put("cliente", cliente.getNombreApellido());
		//params.put("usuario", cliente.getUsername());
		params.put("password", tmpass);
		mailSenderSMTPService.sendMail(from, cliente.getEmail(), subjectRegistroClienteNuevoPassword,
				getBody(params, bodyRegistroClienteNuevoPassword), null);
	}
	
	@Override
	public void notificarHabilitacionExitosa(Cliente cliente) {
		Map<String, String> params = new HashMap<String, String>();
		//params.put("cliente", cliente.getNombreApellido());		
		mailSenderSMTPService.sendMail(from, cliente.getEmail(), subjectHabilitacionExistosa, getBody(params, bodyHabilitacionExistosa), null);
		
	}
	
	
	@Resource(name = "mailSenderSMTPService")
	public void setMailSenderSMTPService(MailSenderSMTPService mailSenderSMTPService) {
		this.mailSenderSMTPService = mailSenderSMTPService;
	}

	
	
	public void setFrom(String from) {
		this.from = from;
	}

	

}
