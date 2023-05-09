package ar.com.avaco.ws.rest.security.service.impl;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ar.com.avaco.ws.rest.security.service.ReporteService;

@Service("reporteService")
public class ReporteServiceImpl implements ReporteService {

	private MailSender mailSender;
	
	@Resource(name = "mailSender")
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	//https://support.hostinger.es/es/articles/3589821-usar-smtp-en-wordpress-hostinger-gmail
	//Could not connect to SMTP host: smtp.hostinger.com, port: 465, response: -1
	@Override
	public void sendMail() {
		JavaMailSenderImpl mail = (JavaMailSenderImpl) mailSender;
		MimeMessage mimeMessage = mail.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo("lucas.silva.t23@gmail.com");
			helper.setText("Prueba");
			mail.send(mimeMessage);
		}catch (Exception e) {
            e.printStackTrace();
        }
	}
}