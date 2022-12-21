package ar.com.avaco.ws.service.filter;

import java.util.ArrayList;
import java.util.List;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.core.domain.filter.FilterData;
import ar.com.avaco.arc.core.domain.filter.FilterDataType;

public class ClienteFilter extends AbstractFilter {

	private String razonSocial;

	private String cuit;

	
	@Override
	public List<FilterData> getFilterDatas() {
		List<FilterData> list = new ArrayList<FilterData>();
		if (razonSocial != null) {
			list.add(new FilterData("razonSocial", razonSocial, FilterDataType.EQUALS));
		}
		if (cuit != null) {
			list.add(new FilterData("cuit", cuit, FilterDataType.EQUALS));
		}
		return list;
	}

	public ClienteFilter() {	}
	
	public ClienteFilter(String razonSocial, String cuit) {
		this.razonSocial = razonSocial;
		this.cuit = cuit;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

}
