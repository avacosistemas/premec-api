package ar.com.avaco.arc.utils.domain.filter;

import java.util.ArrayList;
import java.util.List;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.core.domain.filter.FilterData;
import ar.com.avaco.arc.core.domain.filter.FilterDataType;

public class PersonajeFilter extends AbstractFilter {
	
	private String nombreEquals;
	
	private String nombreLike;
	
	@Override
	public List<FilterData> getFilterDatas() {
		List<FilterData> filterDatas = new ArrayList<FilterData>();
		if (nombreEquals  != null) {
			filterDatas.add(new FilterData("nombre", nombreEquals, FilterDataType.EQUALS));
		}
		if (nombreLike  != null) {
			filterDatas.add(new FilterData("nombre", nombreLike, FilterDataType.LIKE));
		}
		return filterDatas;
	}

	public String getNombreEquals() {
		return nombreEquals;
	}

	public void setNombreEquals(String nombreEquals) {
		this.nombreEquals = nombreEquals;
	}

	public String getNombreLike() {
		return nombreLike;
	}

	public void setNombreLike(String nombreLike) {
		this.nombreLike = nombreLike;
	}
	
	
}
