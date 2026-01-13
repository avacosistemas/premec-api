package ar.com.avaco.ws.service.impl;

import java.awt.Rectangle;
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
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.utils.DateUtils;
import ar.com.avaco.ws.dto.attachment.AttachmentLine;
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
		
		// Por cada recibo
		for (ReciboSueldoDTO recibo : lista) {

			// Obtengo usuario sap
			String usuarioSap = usuarioService.getUsuarioSAPByLegajo(recibo.getLegajo());

			// Obtengo legajo
			String legajo = Integer.toString(recibo.getLegajo());

			String timeInMilis = recibo.getTimeInMilis();
			String descripcion = recibo.getDescripcion();
			
			String[] split = recibo.getPeriodo().split("/");
			String month = split[0];
			String year = split[1];
			
			// Armo el periodo desde/hasta para buscar en sap
			String from = year + month + "01";

			Calendar instance = Calendar.getInstance();
			instance.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
			instance.add(Calendar.MONTH, 1);
			instance.add(Calendar.DAY_OF_MONTH, -1);

			String to = DateUtils.toString(instance.getTime(), "yyyyMMdd");

			// Busco el registros del timesheet con el periodo
			TimeSheetEntryAttach entryAttach = this.timeSheetService.getTimeSheetEntries(new Long(usuarioSap), from,
					to);

			// Nuevo attachment entry del project management timesheet existente
			Long newAttachmentEntry;

			// Si no existe el registro del timesheet lo creo y obtengo el nuevo entry.
			// El attachmententry va a ser null en este caso
			if (entryAttach.getAbsEntry() == null) {
				// Genero el timesheet y luego obtengo el absentry
				Long absEntry = this.timeSheetService.generarTimeSheet(new Long(usuarioSap), from, to);
				entryAttach.setAbsEntry(absEntry);
			}

			// Ya quedo seteado el absEntry (ya sea porque existencia o creacion)

			// Paso al attachment (el pdf del recibo)

			// Creo un attachment y le adjunto el archivo
			List<Map<String, String>> attachments = new ArrayList<>();
			
			Map<String, String> nuevoRecibo = genearAttachmentRecibo(recibo.getTipo(), legajo, usuarioSap, month, year, timeInMilis, descripcion);

			if (entryAttach.getAttachmentEntry() != null) {
				// Si ya existe un attachment en ese timesheet
				// Obtengo el attachment

				ResponseAttachmentGetPost currentAttach = this.attachmentService.getAttachment(entryAttach.getAttachmentEntry());
				
				// Busco si dentro de los adjuntos existe uno con el mismo freetext
				Optional<AttachmentLine> findAny = currentAttach.getAttachments2Lines().stream().filter(actual -> actual.getFreeText().equals(nuevoRecibo.get("FreeText"))).findAny();
				
				// Si no encontré uno igual al que quiero subir, entonces lo subo.
				if (!findAny.isPresent()) {
					attachments.add(nuevoRecibo);
				}
				
				// Luego por cada attachment armo el adjunto en la lista para volver a enviar
				currentAttach.getAttachments2Lines().stream().forEach(att -> {
					attachments.add(genearAttachmentReciboExistente(att.getFreeText(), usuarioSap, att.getSourcePath(), att.getFileName()));
				});

				// De esta manera ya tengo los actuales y el nuevo que voy a agregar
			} else {
				attachments.add(nuevoRecibo);
			}

			
			// Envio el nuevo archivo y los posibles actuales (si existian) y obtengo un
			// nuevo entry
			newAttachmentEntry = this.attachmentService.enviarAttachmentsSap(attachments);

			// Actualizo ProjectManagementTimeSheet
			this.timeSheetService.updateTimeSheetAttachmentEntry(entryAttach.getAbsEntry(), newAttachmentEntry);

		}
	}

	private Map<String, String> genearAttachmentRecibo(String tipo, String legajo, String usuarioSap, String month,
			String year, String timeInMilis, String descripcion) {
		String path = reciboPath + "\\" + year + month;
		String nombre = legajo + "_" + year + month + "_" + tipo + "_" + timeInMilis;

		Map<String, String> fotoMap = new HashMap<String, String>();
		fotoMap.put("SourcePath", path);
		fotoMap.put("FileName", nombre);
		fotoMap.put("FileExtension", "pdf");
		fotoMap.put("UserID", usuarioSap.toString());
		fotoMap.put("Override", "tYES");
		fotoMap.put("FreeText", tipo + "|" + descripcion);
		return fotoMap;
	}

	private Map<String, String> genearAttachmentReciboExistente(String tipo, String usuarioSap, String path, String nombre) {
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

			Rectangle rectPeriodo = new Rectangle(25, 110, 84, 13);
			Rectangle rectDescripcion = new Rectangle(25 + 84, 110, 84 + 20, 13);
			Rectangle rectLegajo = new Rectangle(25, 138, 65, 13);
			Rectangle rectNombre = new Rectangle(91, 138, 205, 13);
			Rectangle rectNeto = new Rectangle(140, 470, 60, 13);

			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			stripper.addRegion("periodo", rectPeriodo);
			stripper.addRegion("descripcion", rectDescripcion);
			stripper.addRegion("legajo", rectLegajo);
			stripper.addRegion("nombre", rectNombre);
			stripper.addRegion("neto", rectNeto);

			for (int i = 0; i < documento.getNumberOfPages(); i++) {

				PDPage page = documento.getPage(i);
				stripper.extractRegions(page);

//				drawTestRectangle(documento, page, recttest.x, recttest.y, recttest.width, recttest.height, Color.BLUE);

				String periodo = stripper.getTextForRegion("periodo").trim().replace("\\n", "").replace("\\n", "");
				String descripcion = stripper.getTextForRegion("descripcion").trim().replace("\\n", "").replace("\\n", "");
				String nombre = stripper.getTextForRegion("nombre").trim().replace("\\n", "").replace("\\n", "");
				String textoLegajo = stripper.getTextForRegion("legajo").trim().replace("\\n", "").replace("\\n", "");
				String textoNeto = stripper.getTextForRegion("neto").trim().replace("\\n", "").replace("\\n", "");

				Integer legajo = Integer.parseInt(textoLegajo);
				BigDecimal neto = new BigDecimal(textoNeto.replace(",", ""));

				stripper.setStartPage(i + 1);
				stripper.setEndPage(i + 1);

				String timeInMillis = ((Long) Calendar.getInstance().getTimeInMillis()).toString();

				ReciboSueldoDTO recibo = new ReciboSueldoDTO(legajo, nombre, periodo, neto, tipo, descripcion, timeInMillis);
				recibos.add(recibo);

				String month = periodo.split("/")[0];
				String year = periodo.split("/")[1];

				String folder = reciboPath + "\\" + year + month;
				Files.createDirectories(Paths.get(folder));
				String baseName = folder + "\\" + legajo + "_" + year + month + "_" + tipo + "_" + timeInMillis;
				
				// Guardar PDF individual
				PDPage pagina = documento.getPage(i);
				PDDocument salida = new PDDocument();
				salida.addPage(pagina);
				salida.save(baseName +  ".pdf");
				salida.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return recibos;
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

		// Obtengo el usuario logueado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		this.logger.debug("Username: " + username);

		// Obtengo el usuario SAP
		String usuarioSAP = usuarioService.getUsuarioSAPByUsername(username);
		this.logger.debug("Usuario Sap: " + usuarioSAP);

		// Si tiene usuario sap
		if (StringUtils.isNotBlank(usuarioSAP)) {

			// Busco los attachments del usuario sap
			List<ProjectManagementTimeSheetAttachDTO> registros = this.timeSheetService
					.listTimeSheetByUsuarioSap(new Long(usuarioSAP));

			List<RegistroReciboPorUsuarioDTO> recibos = new ArrayList<>();

			// Por cada registro
			registros.stream().forEach(registro -> {

				this.logger.debug("Procesando Registro: " + registro.getAttachmentEntry());

				// Armo el periodo
				int month = Integer.parseInt(registro.getDateFrom().split("-")[1]);
				int year = Integer.parseInt(registro.getDateFrom().split("-")[0]);

				Calendar instance = Calendar.getInstance();
				instance.set(Calendar.MONTH, month - 1);

				SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
				String monthString = formatoMes.format(instance.getTime());

				
				// Por cada attachment que tenga el periodo (seria un recibo por attachment)
				registro.getAttachments2().getLines().stream().forEach(tipo -> {
					this.logger.debug("Procesando Attach: " + tipo.getFileName() + " " + tipo.getFreeText());
					
					// Armo el registro
					RegistroReciboPorUsuarioDTO r = new RegistroReciboPorUsuarioDTO();
					r.setAttachmentEntry(registro.getAttachmentEntry());
					r.setAbsEntry(tipo.getAbsoluteEntry());
					r.setMonth(month);
					r.setYear(year);
					r.setMonthString(monthString);
					r.setFilePath(tipo.getTargetPath() + "\\" + tipo.getFileName() + "." + tipo.getFileExtension());
					int idx = tipo.getFreeText().indexOf('|');
					String reciboTipo = (idx >= 0) ? tipo.getFreeText().substring(0, idx) : tipo.getFreeText();
					String descripcion = (idx >= 0) ? tipo.getFreeText().substring(idx + 1, tipo.getFreeText().length()) : "";

					r.setDescripcion(descripcion);
					r.setTipo(reciboTipo);
					recibos.add(r);

				});

			});
			return recibos;
		} else {
			throw new ErrorValidationException("El usuario no tiene asociado usuario sap", null);
		}
	}

	@Override
	public byte[] obtenerReciboPDF(RegistroReciboPorUsuarioDTO recibo) throws IOException {
		Path path = Paths.get(recibo.getFilePath().replace("\\", "\\\\"));
		byte[] contenido = Files.readAllBytes(path);
		return contenido;
	}

	@Override
	public byte[] obtenerReciboPDF(Long absEntry, Long attEntry) throws IOException {

		ResponseAttachmentGetPost attachment = this.attachmentService.getAttachment(attEntry);
		AttachmentLine line = attachment.getAttachments2Lines().stream().filter(x -> x.getAbsoluteEntry().equals(absEntry)).findAny().get();
		
//		Path path = Paths.get(reciboPathServeSap + "\\" + legajo + "_" + year + month + "_" + recibo.getTipo() + ".pdf");
		Path path = Paths.get(line.getTargetPath() + "\\" + line.getFileName() + "." + line.getFileExtension());
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
