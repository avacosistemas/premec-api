/**
 * 
 */
package ar.com.avaco.ws.rest.service.filter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.core.domain.filter.FilterData;
import ar.com.avaco.arc.core.domain.filter.FilterDataType;

/**
 * @author avaco
 *
 */
public class FaqFilter extends AbstractFilter {

	private Map<String, String> customQuery;
	
	
	public FaqFilter(Map<String, String> customQuery) {
		this.customQuery = customQuery;
	}

	@Override
	public List<FilterData> getFilterDatas() {
		List<FilterData> filters = super.getFilterDatas();
		for (Entry<String, String> map: customQuery.entrySet()) {
			filters.add(new FilterData(map.getKey(), map.getValue(), FilterDataType.EQUALS));
		}
		return filters;
	}
}
