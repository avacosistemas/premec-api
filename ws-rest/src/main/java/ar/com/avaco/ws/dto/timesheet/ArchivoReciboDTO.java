package ar.com.avaco.ws.dto.timesheet;

public class ArchivoReciboDTO {

	private byte[] archivo;

	private String tipo;

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
