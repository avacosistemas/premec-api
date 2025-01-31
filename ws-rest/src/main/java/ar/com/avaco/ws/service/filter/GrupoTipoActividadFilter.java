package ar.com.avaco.ws.service.filter;

import java.util.ArrayList;
import java.util.List;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.core.domain.filter.FilterData;
import ar.com.avaco.arc.core.domain.filter.FilterDataType;
import ar.com.avaco.commons.domain.TipoActividad;

public class GrupoTipoActividadFilter extends AbstractFilter {

	private TipoActividad tipoActividad;

	@Override
	public List<FilterData> getFilterDatas() {
		List<FilterData> list = new ArrayList<FilterData>();
		list.add(new FilterData("tipo", tipoActividad, FilterDataType.EQUALS));
		return list;
	}

	public TipoActividad getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(TipoActividad tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

}
