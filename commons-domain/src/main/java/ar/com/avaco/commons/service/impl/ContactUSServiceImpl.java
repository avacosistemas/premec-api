/**
 * 
 */
package ar.com.avaco.commons.service.impl;

import java.io.StringWriter;
import java.util.Date;

import javax.annotation.Resource
;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.avaco.arc.core.component.bean.service.NJBaseService;
import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.commons.domain.ContactUS;
import ar.com.avaco.commons.repository.ContactUSRepository;
import ar.com.avaco.commons.service.ContactUSService;

/**
 * @author avaco
 */

@Transactional
@Service("contactUSService")
public class ContactUSServiceImpl extends NJBaseService<Long, ContactUS, ContactUSRepository> implements ContactUSService {

	private static final String CLASSPATH_RESOURCE_LOADER_CLASS = "classpath.resource.loader.class";
	private static final String CLASSPATH = "classpath";
	private static final String CLIENT = "client";
	private static final String ISO_8859_1 = "ISO-8859-1";

	@Value("Contacto")
	private String contactUSSubjetClient;

	@Value("Contacto")
	private String contactUSSubjetSystem;
	
	@Value("template/contact-us-template-client.html")
	private String contactUSTemplateClient;
	
	@Value("template/contact-us-template-system.html")
	private String contactUSTemplateSystem;
	
	@Value("none@gmail.com")
	private String contactUSFrom;
	
	@Resource(name = "mailSenderSMTPService")
	private MailSenderSMTPService mailSenderSMTPService;
	
	private VelocityEngine ve;
	
	public ContactUSServiceImpl() {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, CLASSPATH);
		ve.setProperty(CLASSPATH_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
		ve.init();
		this.ve = ve;
	}
	
	@Resource(name = "contactUSRepository")
	public void setContactUSRepository(ContactUSRepository contactUSRepository) {
		this.repository = contactUSRepository;
	}
	
	public ContactUS send(ContactUS entity) {
		entity.setDate(new Date());
		this.notifyClient(entity);
		this.notifyLikewise(entity);
		return super.save(entity);
	}

	private void notifyClient(ContactUS contact) {
		StringWriter writer = getMessageTemplate(contactUSTemplateClient,contact);
		mailSenderSMTPService.sendMail(contactUSFrom, contact.getEmail(), contactUSSubjetClient, writer.toString(), null);
	}

	private StringWriter getMessageTemplate(String templateFile,ContactUS contact) {
		VelocityContext context = new VelocityContext();
		context.put( CLIENT, contact);
		Template template = ve.getTemplate(templateFile, ISO_8859_1);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		System.out.println(writer.toString());
		return writer;
	}
	
	private void notifyLikewise(ContactUS contact) {
		StringWriter writer = getMessageTemplate(contactUSTemplateSystem, contact);
		mailSenderSMTPService.sendMail(contactUSFrom, contactUSFrom, contactUSSubjetSystem, writer.toString(), null);
	}
	
}
