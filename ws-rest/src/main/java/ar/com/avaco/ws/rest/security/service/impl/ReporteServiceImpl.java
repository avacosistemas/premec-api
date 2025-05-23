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

	@Autowired
	private MailSenderSMTPService mailSenderSMTPService;

	@Override
	public void sendMail(String email, String activityCode, String bodyMail, String subject) {

		File file = new File(informePath + "\\" + activityCode + ".pdf");

		List<File> files = new ArrayList<>();
		files.add(file);

		if (StringUtils.isBlank(email)) {
			mailSenderSMTPService.sendMail(emailFrom, emailFrom, subject,
					"Cliente sin Correo definido", files);
		} else {
			mailSenderSMTPService.sendMail(emailFrom, email, emailCC, subject,
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