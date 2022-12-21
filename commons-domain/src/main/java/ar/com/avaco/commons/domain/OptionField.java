/**
 * 
 */
package ar.com.avaco.commons.domain;

/**
 * @author avaco
 *
 */
public class OptionField {
	private String format; // Usado en campos tipo fecha
	private String matchTo; // Usado en un campo de filtro 

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getMatchTo() {
		return matchTo;
	}

	public void setMatchTo(String matchTo) {
		this.matchTo = matchTo;
	}
	
}
