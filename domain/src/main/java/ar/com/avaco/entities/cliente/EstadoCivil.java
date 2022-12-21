package ar.com.avaco.entities.cliente;

public enum EstadoCivil {

	CASADO("Casado"), 
	SOLTERO("Soltero"), 
	VIUDO("Viudo"), 
	DIVORCIADO("Divorciado");
	
	private String label;

	EstadoCivil(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	
}
