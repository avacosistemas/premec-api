package ar.com.avaco.ws.rest.service;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;

public abstract class AbstractNotificacionService {

	protected static final String CLASSPATH_RESOURCE_LOADER_CLASS = "classpath.resource.loader.class";
	protected static final String CLASSPATH = "classpath";
	protected static final String ISO_8859_1 = "ISO-8859-1";
	
	protected VelocityEngine ve;

	protected MailSenderSMTPService mailSenderSMTPService;
	
	@Value("${prop.header.general}")
	private String headerGeneral;
	
	@Value("${prop.footer.general}")
	private String footerGeneral;
	
	public AbstractNotificacionService() {
		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, CLASSPATH);
		ve.setProperty(CLASSPATH_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
		ve.init();
	}
	
	public abstract void setMailSenderSMTPService(MailSenderSMTPService mailSenderSMTPService);
	
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
		String body = writer.toString();
		return new StringBuilder().append(getHeader()).append(body).append(getFooter()).toString();
	}

	
}
