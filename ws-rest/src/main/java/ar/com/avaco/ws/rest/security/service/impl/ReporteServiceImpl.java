package ar.com.avaco.ws.rest.security.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.ws.rest.security.service.ReporteService;

@Service("reporteService")
public class ReporteServiceImpl implements ReporteService {

	@Value("${informe.path}")
	private String informePath;

	@Value("${email.from}")
	private String emailFrom;

	@Value("${email.cc}")
	private String emailCC;

	@Value("${email.bodyMail}")
	private String bodyMail;

	@Autowired
	private MailSenderSMTPService mailSenderSMTPService;

	// https://support.hostinger.es/es/articles/3589821-usar-smtp-en-wordpress-hostinger-gmail
	// Could not connect to SMTP host: smtp.hostinger.com, port: 465, response: -1
	@Override
	public void sendMail(String email, String activityCode) {

		File file = new File(informePath + activityCode + ".pdf");
		List<File> files = new ArrayList<>();
		files.add(file);

		if (StringUtils.isBlank(email)) {
			mailSenderSMTPService.sendMail(emailFrom, emailFrom, "PREMEC SA - INFORME DE SERVICIO TECNICO",
					"Cliente sin Correo definido", files);
		} else {
			mailSenderSMTPService.sendMail(emailFrom, email, emailCC, "PREMEC SA - INFORME DE SERVICIO TECNICO",
					bodyMail, files);
		}

	}

	public void setMailSenderSMTPService(MailSenderSMTPService mailSenderSMTPService) {
		this.mailSenderSMTPService = mailSenderSMTPService;
	}

	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
	}
}