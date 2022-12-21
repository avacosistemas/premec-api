package ar.com.avaco.arc.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("mailSenderSMTPService")
public class MailSenderSMTPServiceImpl implements MailSenderSMTPService {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private MailSender mailSender;
	
	/**
	 * @param mailSender
	 */
	@Resource(name = "mailSender")
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	/**
	 * sendMail
	 * 
	 * @param from String
	 * @param to String
	 * @param subject String
	 * @param msg List<String>
	 * @param archivos List<File>
	 */
	@Async
	public void sendMail(String from, String to, String subject, String msg, List<File> archivos){
		List<String> messages = new ArrayList<String>();
		messages.add(msg);
		String[] arrayTo = {to};
		sendMail(from, arrayTo, null, subject, messages, archivos);
	}
	
	@Override
	public void sendMail(String from, String[] to, String[] bccTo, String subject, String msg, List<File> archivos) {
		List<String> messages = new ArrayList<String>();
		messages.add(msg);
		sendMail(from, to, bccTo, subject, messages, archivos);		
	}
	
	/**
	 * sendMail
	 * 
	 * @param from String
	 * @param to String[]
	 * @param asunto String
	 * @param msg List<String>
	 * @param archivos List<File>
	 */
	@Async
	public void sendMail(String from, String[] to, String[] bccTo, String asunto, List<String> msg, List<File> archivos){
		JavaMailSenderImpl mail = (JavaMailSenderImpl) mailSender;
		MimeMessage mimeMessage = mail.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		StringBuilder sbText = new StringBuilder();
		
		try {
			helper.setFrom(from);
			if(to == null){
				throw new MessagingException("No esta seteado el destinatario");
			}
			if (to != null) {
				helper.setTo(to);
			}
			if (bccTo != null) {
				helper.setBcc(bccTo);
			}
			helper.setSubject(asunto);			
			for (String body : msg) {
				sbText.append(body + "\n<br>");
			}
			helper.setText(sbText.toString(), true);
			if(archivos != null && archivos.size() > 0){
				for (File archivo : archivos) {
					helper.addAttachment(archivo.getName(), archivo);
				}				
			}
			mail.send(mimeMessage);
			
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < to.length; i++) {
				sb.append(to[i] + "; ");
			}
			String errorMessage = "Error al enviar el mail a: " + sb.toString() + "\n" +
			               "Asunto: " + asunto + "\n" +
					       "Cuerpo: " + sbText.toString();
			logger.error(errorMessage , e);
		}
	}

	
}