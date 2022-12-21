/**
 * 
 */
package ar.com.avaco.filter;

import java.util.List;
import java.util.Map;

import ar.com.avaco.arc.core.domain.filter.AbstractFilter;
import ar.com.avaco.arc.core.domain.filter.FilterData;
import ar.com.avaco.arc.core.domain.filter.FilterDataType;
import ar.com.avaco.commons.domain.FieldDef;

/**
 * @author avaco
 *
 */
public class MapFormFilter extends AbstractFilter {

	private Map<String, String> customQuery;
	private List<FieldDef> fieldDefs;
	
	public MapFormFilter(Map<String, String> customQuery, List<FieldDef> fieldDefs) {
		this.customQuery = customQuery;
		this.fieldDefs = fieldDefs;
	}

	@Override
	public List<FilterData> getFilterDatas() {
		List<FilterData> filters = super.getFilterDatas();
		for (FieldDef field: this.fieldDefs) {
			String value = this.customQuery.get(field.getKey());
			if(value != null) {				
				if(field.getOptions() != null && field.getOptions().getMatchTo() != null) {
					filters.add(new FilterData(field.getOptions().getMatchTo(), this.customQuery.get(field.getKey()), getFilterType(field.getFilterType())));
				}else {
					filters.add(new FilterData(field.getKey(), this.customQuery.get(field.getKey()), getFilterType(field.getFilterType())));
				}
			}
		}
		return filters;
	}

	private FilterDataType getFilterType(String filterType) {
		switch (filterType.toUpperCase()) {
			case "EQUALS": return FilterDataType.EQUALS;
			case "LIKE": return FilterDataType.LIKE;
			case "LESS-THAN": return FilterDataType.LESS_THAN;
			case "GREATER-THAN": return FilterDataType.MORE_THAN;
			case "LESS-EQUALS": return FilterDataType.EQUALS_LESS_THAN;
			case "GREATER-EQUALS": return FilterDataType.EQUALS_MORE_THAN;
		}
		return null;
	}
}
