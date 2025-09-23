package ar.com.avaco.ws.dto;

import java.util.List;

public class ODataResponse<T> {
	private List<T> value;

	public List<T> getValue() {
		return value;
	}

	public void setValue(List<T> value) {
		this.value = value;
	}

}
