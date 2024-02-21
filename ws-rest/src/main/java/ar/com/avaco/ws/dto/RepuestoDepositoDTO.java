package ar.com.avaco.ws.dto;

public class RepuestoDepositoDTO {

	private String itemCode;
	private String itemName;
	private Double stock;
	private boolean seriado;

	public boolean isSeriado() {
		return seriado;
	}

	public void setSeriado(boolean seriado) {
		this.seriado = seriado;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

}
