package ar.com.avaco.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public final static String PATTERN_yyyyMMdd = "yyyyMMdd";
	public final static String PATTERN_FULL_24_HS = "dd/MM/yyyy HH:mm:ss";

	public static Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate toLocalDate(String fecha, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(fecha, formatter);
	}

	public static String toString(LocalDate ld, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return ld.format(formatter);
	}

	public static Date toDate(String dia, String pattern) {
		SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
		dateFormat.applyPattern(pattern);
		try {
			return dateFormat.parse(dia);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date toDate(String dia) throws ParseException {
		return toDate(dia, PATTERN_yyyyMMdd);
	}

	public static String toString(Date fecha) {
		return toString(fecha, "dd/MM/yyyy");
	}

	public static String toString(Date fecha, String pattern) {
		SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
		dateFormat.applyPattern(pattern);
		return dateFormat.format(fecha);
	}

	public static String toString(LocalDate fecha) {
		SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
		dateFormat.applyPattern(PATTERN_yyyyMMdd);
		return dateFormat.format(fecha);
	}

	public static Date getFechaYHoraActual() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	public static Date setearHoraCero(Date fecha) {
		Calendar anteriorCal = Calendar.getInstance();
		anteriorCal.setTime(fecha);
		anteriorCal.set(Calendar.HOUR, 0);
		anteriorCal.set(Calendar.MINUTE, 0);
		anteriorCal.set(Calendar.SECOND, 0);
		return anteriorCal.getTime();
	}
	
	public static String convertirSinTimeZome(String fechaStr) {
		LocalDate fecha;
		if (fechaStr.contains("T")) {
		    fecha = OffsetDateTime.parse(fechaStr).toLocalDate();
		} else {
		    fecha = LocalDate.parse(fechaStr);
		}

		String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));  
        return fechaFormateada;
	}
	
	public static int convertirATotalSegundos(String tiempo) {
		String[] partes = tiempo.split(":");
		int horas = Integer.parseInt(partes[0]);
		int minutos = Integer.parseInt(partes[1]);
		int segundos = Integer.parseInt(partes[2]);
		return horas * 3600 + minutos * 60 + segundos;
	}

	public static String convertirSegundosAFormato(int segundos) {
		Duration duration = Duration.ofSeconds(segundos);
		long horas = duration.toHours();
		long minutos = duration.minusHours(horas).toMinutes();
		long segs = duration.minusHours(horas).minusMinutes(minutos).getSeconds();
		return String.format("%02d:%02d:%02d", horas, minutos, segs);
	}

	public static String calcularDiferenciaHorario(String desde, String hasta) {
		
		if (desde.length() != 8) desde = desde  + ":00";
		if (hasta.length() != 8) hasta = hasta  + ":00";
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime horaHasta = LocalTime.parse(hasta.trim(), formatter);
		LocalTime horaDesde = LocalTime.parse(desde.trim(), formatter);

		String diferencia = null;

		Duration d = Duration.between(horaDesde, horaHasta);

		if (d.isZero()) {
			diferencia = "00:00:00";
		} else if (!d.isNegative()) {
			long hours = d.toHours();
			long minutes = d.toMinutes() % 60;
			long seconds = d.getSeconds() % 60;
			diferencia = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}
		return diferencia;

	}

}
