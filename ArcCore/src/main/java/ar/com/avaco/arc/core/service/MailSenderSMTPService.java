package ar.com.avaco.arc.core.service;

import java.io.File;
import java.util.List;

public interface MailSenderSMTPService {
	
	/**
	 * Envia un mail basico con archivos
	 * @param from
	 * @param to
	 * @param subject
	 * @param msg
	 * @param archivos
	 */
	public void sendMail(String from, String to, String subject, String msg, List<File> archivos);
	
	/**
	 * Envia un mail con varios textos y archivos
	 * @param from
	 * @param to
	 * @param subject
	 * @param msg
	 * @param archivos
	 */
	public void sendMail(String from, String[] to, String[] bccTo, String subject, String msg, List<File> archivos);
	
	public void sendMail(String from, String[] to, String[] bccTo, String asunto, List<String> msg, List<File> archivos);
	
}