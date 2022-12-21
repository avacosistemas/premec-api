package ar.com.avaco.arc.core.domain.filter;

/**
 * @author avaco
 *
 */
public class FilterData {

	private String property;
	private Object object;
	private FilterDataType filterDataType;

	public FilterData(String property, Object object, FilterDataType filterDataType) {
		super();
		this.property = property;
		this.object = object;
		this.filterDataType = filterDataType;
	}

	public String getProperty() {
		return property;
	}

	public Object getObject() {
		return object;
	}

	public FilterDataType getFilterDataType() {
		return filterDataType;
	}

}