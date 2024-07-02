package ar.com.avaco.arc.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("mailSenderSMTPService")
public class MailSenderSMTPServiceImpl implements MailSenderSMTPService {

	private Logger logger = Logger.getLogger(this.getClass());

	private MailSender mailSender;

	@Value("${email.test}")
	private boolean test;

	@Value("${email.enabled}")
	private boolean enabled;

	@Value("${email.from}")
	private String from;

	@Value("${email.errores}")
	private String toErrores;

	@Value("${email.errores.cc}")
	private String toErroresCC;

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
	 * @param from     String
	 * @param to       String
	 * @param subject  String
	 * @param msg      List<String>
	 * @param archivos List<File>
	 */
	@Async
	public void sendMail(String from, String to, String subject, String msg, List<File> archivos) {
		List<String> messages = new ArrayList<String>();
		messages.add(msg);
		String[] arrayTo = { to };
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
	 * @param from     String
	 * @param to       String[]
	 * @param asunto   String
	 * @param msg      List<String>
	 * @param archivos List<File>
	 */
	@Async
	public void sendMail(String from, String[] to, String[] bccTo, String asunto, List<String> msg,
			List<File> archivos) {
		try {
			JavaMailSenderImpl mail = (JavaMailSenderImpl) mailSender;
			MimeMessage mimeMessage = mail.createMimeMessage();
			MimeMessageHelper helper;
			helper = new MimeMessageHelper(mimeMessage, true);
			StringBuilder sbText = new StringBuilder();

			helper.setFrom(from);
			if (to == null) {
				throw new MessagingException("No esta seteado el destinatario");
			}
			if (to != null) {
				helper.setTo(to);
			}
			if (bccTo != null) {
				helper.setBcc(bccTo);
			}

			if (test) {
				asunto = "[TEST] - " + asunto;
			}

			helper.setSubject(asunto);
			for (String body : msg) {
				sbText.append(body + "\n<br>");
			}
			helper.setText(sbText.toString(), true);
			if (archivos != null && archivos.size() > 0) {
				for (File archivo : archivos) {
					helper.addAttachment(archivo.getName(), archivo);
				}
			}
			if (enabled)
				mail.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void sendMail(String from, String to, String bccTo, String subject, String msg, List<File> archivos) {
		List<String> messages = new ArrayList<String>();
		messages.add(msg);
		String[] arrayTo = { to };
		String[] arrayBcc = { bccTo };
		sendMail(from, arrayTo, arrayBcc, subject, messages, archivos);

	}

	@Override
	public void sendMail(String subject, String msg, List<File> archivos) {
		List<String> messages = new ArrayList<String>();
		messages.add(msg);
		String[] arrayTo = { toErrores };
		String[] arrayBcc = { toErroresCC };
		sendMail(from, arrayTo, arrayBcc, subject, messages, archivos);
		
	}

}