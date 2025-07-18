package ar.com.avaco.ws.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.utils.DateUtils;
import ar.com.avaco.ws.dto.attachment.ResponseAttachmentGetPost;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetAttachDTO;
import ar.com.avaco.ws.dto.timesheet.ReciboSueldoDTO;
import ar.com.avaco.ws.dto.timesheet.RegistroReciboPorUsuarioDTO;
import ar.com.avaco.ws.service.AbstractSapService;
import ar.com.avaco.ws.service.ReciboSueldoService;

@Service("reciboSueldoService")
public class ReciboSueldoServiceImpl extends AbstractSapService implements ReciboSueldoService {

	@Value("${email.contador.from}")
	private String emailFromContador;

	@Value("${email.contador.to}")
	private String emailToContador;

	@Value("${email.contador.subject.rechazo}")
	private String subjectRechazo;

	@Value("${email.contador.body.rechazo}")
	private String bodyRechazo;

	@Value("${path.recibo}")
	private String reciboPath;

	@Value("${path.recibo.serversap}")
	private String reciboPathServeSap;

	@Autowired
	private MailSenderSMTPService sender;

	private UsuarioService usuarioService;

	private TimeSheetService timeSheetService;

	private Logger logger = Logger.getLogger(this.getClass());

	private AttachmentService attachmentService;

	@Override
	public void aprobarRecibos(List<ReciboSueldoDTO> lista) {
		for (ReciboSueldoDTO recibo : lista) {
			String usuarioSap = usuarioService.getUsuarioSAPByLegajo(recibo.getLegajo());

			String legajo = Integer.toString(recibo.getLegajo());

			String[] split = recibo.getPeriodo().split("/");
			String month = split[0];
			String year = split[1];

			String from = year + month + "01";

			Calendar instance = Calendar.getInstance();
			instance.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
			instance.add(Calendar.MONTH, 1);
			instance.add(Calendar.DAY_OF_MONTH, -1);

			String to = DateUtils.toString(instance.getTime(), "yyyyMMdd");

			// Busco el registros del timesheet con el periodo
			TimeSheetEntryAttach entryAttach = this.timeSheetService.getTimeSheetEntries(usuarioSap, from, to);

			// Nuevo attachment entry del project management timesheet existente
			Long newAttachmentEntry;

			// Si no existe el registro del timesheet lo creo y obtengo el nuevo entry.
			// El attachmententry va a ser null en este caso
			if (entryAttach.getAbsEntry() == null) {
				// Genero el timesheet y luego obtengo el absentry
				Long absEntry = this.timeSheetService.generarTimeSheet(usuarioSap, from, to);
				entryAttach.setAbsEntry(absEntry);
			}

			// Ya quedo seteado el absEntry (ya sea porque existencia o creacion)

			// Paso al attachment (el pdf del recibo)

			// Creo un attachment y le adjunto el archivo
			List<Map<String, String>> attachments = new ArrayList<>();

			attachments.add(genearAttachmentRecibo(recibo.getTipo(), legajo, usuarioSap, month, year));

			if (entryAttach.getAttachmentEntry() != null) {
				// Si ya existe un attachment en ese timesheet
				// Obtengo el attachment

				ResponseAttachmentGetPost currentAttach = this.attachmentService
						.getAttachment(entryAttach.getAttachmentEntry());

				// por cada attachment armo el adjunto en la lista para volver a enviar
				currentAttach.getAttachments2Lines().stream().forEach(att -> {
					attachments.add(genearAttachmentRecibo(att.getFreeText(), legajo, usuarioSap, month, year));
				});

				// De esta manera ya tengo los actuales y el nuevo que voy a agregar
			}

			// Envio el nuevo archivo y los posibles actuales (si existian) y obtengo un
			// nuevo entry
			newAttachmentEntry = this.attachmentService.enviarAttachmentsSap(attachments);

			// Actualizo ProjectManagementTimeSheet
			this.timeSheetService.updateTimeSheetAttachmentEntry(entryAttach.getAbsEntry(), newAttachmentEntry);

		}
	}

	private Map<String, String> genearAttachmentRecibo(String tipo, String legajo, String usuarioSap, String month,
			String year) {
		String path = reciboPath + "\\" + year + month;
		String nombre = legajo + "_" + year + month + "_" + tipo;

		Map<String, String> fotoMap = new HashMap<String, String>();
		fotoMap.put("SourcePath", path);
		fotoMap.put("FileName", nombre);
		fotoMap.put("FileExtension", "pdf");
		fotoMap.put("UserID", usuarioSap.toString());
		fotoMap.put("Override", "tYES");
		fotoMap.put("FreeText", tipo);
		return fotoMap;
	}

	

	@Override
	public List<ReciboSueldoDTO> procesarRecibos(String tipo, byte[] archivo) {
		List<ReciboSueldoDTO> recibos = new ArrayList<>();
		try {
			PDDocument documento = PDDocument.load(new ByteArrayInputStream(archivo));
			PDFTextStripper stripper = new PDFTextStripper();

			for (int i = 0; i < documento.getNumberOfPages(); i++) {
				stripper.setStartPage(i + 1);
				stripper.setEndPage(i + 1);
				String texto;
				texto = stripper.getText(documento);
				// Parseo de datos
				String nombre = extraerNombre(texto);
				Integer legajo = extraerLegajo(texto);
				String periodoRecibo = extraerPeriodo(texto);

				BigDecimal neto = extraerNeto(texto);

				if (nombre != null && legajo != null && periodoRecibo != null && neto != null) {

					ReciboSueldoDTO recibo = new ReciboSueldoDTO(legajo, nombre, periodoRecibo, neto, tipo);
					recibos.add(recibo);

					String month = periodoRecibo.split("/")[0];
					String year = periodoRecibo.split("/")[1];

					String folder = reciboPath + "\\" + year + month;
					Files.createDirectories(Paths.get(folder));
					String baseName = folder + "\\" + legajo + "_" + year + month + "_" + tipo;
					// Guardar PDF individual
					PDPage pagina = documento.getPage(i);
					PDDocument salida = new PDDocument();
					salida.addPage(pagina);
					salida.save(baseName + ".pdf");
					salida.close();

				} else {
					throw new ErrorValidationException("No se pudo parsear el archivo de recibos en la pagina " + i,
							null);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return recibos;
	}

	private String extraerNombre(String texto) {
		// 1. Cortamos el texto hasta la primera aparición de "Apellido y nombre del
		// empleado"
		int indexFinNombre = texto.indexOf("Apellido y nombre del empleado");
		if (indexFinNombre == -1)
			return null;

		String bloqueNombre = texto.substring(0, indexFinNombre);

		// 2. Buscamos el último candidato tipo "Apellido , Nombre" en ese bloque
		Pattern pattern = Pattern.compile("([A-ZÁÉÍÓÚÑ][a-záéíóúñ]+\\s*,\\s*[A-ZÁÉÍÓÚÑa-záéíóúñ\\s\\.]+)");
		Matcher matcher = pattern.matcher(bloqueNombre);

		String ultimoMatch = null;
		while (matcher.find()) {
			ultimoMatch = matcher.group(1).trim();
		}

		return ultimoMatch;
	}

	private Integer extraerLegajo(String texto) {
		Matcher m = Pattern.compile("(?m)^\\s*(\\d{2,4})\\s*$").matcher(texto);
		return m.find() ? Integer.parseInt(m.group(1)) : null;
	}

	private String extraerPeriodo(String texto) {
		// Buscar el patrón MM/YYYY al principio de una línea
		Pattern pattern = Pattern.compile("(?m)^\\s*(\\d{2}/\\d{4})\\b");
		Matcher matcher = pattern.matcher(texto);

		// Devolver el primer match que aparezca
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private BigDecimal extraerNeto(String texto) {
		Pattern pattern = Pattern.compile("([\\d,.]+)\\s*:\\s*Neto");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			try {
				// Reemplazamos la coma por vacío para convertir correctamente a BigDecimal
				String numero = matcher.group(1).replace(",", "");
				return new BigDecimal(numero);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public void rechazarRecibos(List<ReciboSueldoDTO> lista) {
		String msj = bodyRechazo.replace("%detalle%", generarMensajeRechazo(lista));
		sender.sendMail(emailFromContador, emailToContador, subjectRechazo, msj, null);
	}

	private String generarMensajeRechazo(List<ReciboSueldoDTO> lista) {
		String body = "";
		for (ReciboSueldoDTO recibo : lista) {
			body += "<tr>";
			body += "<td>" + recibo.getLegajo() + "</td>";
			body += "<td>" + recibo.getNombreCompleto() + "</td>";
			body += "<td>" + recibo.getPeriodo() + "</td>";
			body += "<td>" + recibo.getTipo() + "</td>";
			body += "<td>" + recibo.getNeto() + "</td>";
			body += "<td>" + recibo.getObservaciones() + "</td>";
			body += "</tr>";
		}
		return body;
	}

	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public List<RegistroReciboPorUsuarioDTO> listarRecibosPorUsuario() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		this.logger.debug("Username: " + username);

		String usuarioSAP = usuarioService.getUsuarioSAPByUsername(username);
		this.logger.debug("Usuario Sap: " + usuarioSAP);

		List<ProjectManagementTimeSheetAttachDTO> registros = this.timeSheetService.listTimeSheetByUsuarioSap(usuarioSAP);

		List<RegistroReciboPorUsuarioDTO> recibos = new ArrayList<>();

		registros.stream().forEach(registro -> {

			this.logger.debug("Procesando Registro: " + registro.getAttachmentEntry());

			int month = Integer.parseInt(registro.getDateFrom().split("-")[1]);
			int year = Integer.parseInt(registro.getDateFrom().split("-")[0]);

			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.MONTH, month - 1);

			SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
			String monthString = formatoMes.format(instance.getTime());

			registro.getAttachments2().getLines().stream().forEach(tipo -> {
				this.logger.debug("Procesando Attach: " + tipo.getFileName() + " " + tipo.getFreeText());
				RegistroReciboPorUsuarioDTO r = new RegistroReciboPorUsuarioDTO();
				r.setAttachmentEntry(registro.getAttachmentEntry());
				r.setMonth(month);
				r.setYear(year);
				r.setMonthString(monthString);
				r.setTipo(tipo.getFreeText());
				recibos.add(r);

			});

		});
		return recibos;
	}

	

	@Override
	public byte[] obtenerReciboPDF(RegistroReciboPorUsuarioDTO recibo) throws IOException {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Integer legajo = usuarioService.findByUsername(name).getLegajo();
		Integer year = recibo.getYear();
		String month = StringUtils.leftPad(recibo.getMonth().toString(), 2, "0");
		Path path = Paths
				.get(reciboPathServeSap + "\\" + legajo + "_" + year + month + "_" + recibo.getTipo() + ".pdf");
		byte[] contenido = Files.readAllBytes(path);
		return contenido;
	}

	@Resource(name = "timeSheetService")
	public void setTimeSheetService(TimeSheetService timeSheetService) {
		this.timeSheetService = timeSheetService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

}
