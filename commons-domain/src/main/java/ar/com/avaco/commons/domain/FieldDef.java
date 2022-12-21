/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.util.List;

/**
 * @author avaco
 *
 */

public class FieldDef {
	public static final String TYPE_STRING = "STRING";
	public static final String TYPE_EMAIL = "EMAIL";
	public static final String TYPE_NUMBER = "NUMBER";
	public static final String TYPE_INTEGER = "INTEGER";
	public static final String TYPE_FLOAT = "FLOAT";
	public static final String TYPE_DATE = "DATE";
	public static final String TYPE_ARRAY = "ARRAY";
	public static final String TYPE_BOOLEAN = "BOOLEAN";
	public static final String TYPE_OBJECT = "OBJECT";
	public static final String TYPE_LIST = "LIST";
	private String key;
	private String labelKey;
	private String label;
	private String type;
	private Boolean required;
	private Boolean disabled;
	private String controlType;
	private String filterType;
	private Integer minValue;
	private Integer maxValue;
	private Integer length;
	private Integer minLength;
	private Integer maxLength;
	private OptionField options;
	private FormDef formDef;
	private String relation;
	private List<ValidationField> validations;
	
	
	public FieldDef() {
		super();
	}
	
	public FieldDef(String key, String type, Boolean required) {
		super();
		this.key = key;
		this.type = type;
		this.required = required;
	}

	public FieldDef(String key, String type, Boolean required, FormDef formDef) {
		super();
		this.key = key;
		this.type = type;
		this.required = required;
		this.formDef = formDef;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	public Integer getMinValue() {
		return minValue;
	}
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}
	public String getType() {
		return type != null ? type.toUpperCase() : null;
	}
	public void setType(String type) {
		this.type = type != null ? type.toUpperCase() : null;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
	public OptionField getOptions() {
		return options;
	}
	public void setOptions(OptionField options) {
		this.options = options;
	}
	public List<ValidationField> getValidations() {
		return validations;
	}
	public void setValidations(List<ValidationField> validations) {
		this.validations = validations;
	}
	public String getLabelKey() {
		return labelKey;
	}
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public FormDef getFormDef() {
		return formDef;
	}

	public void setFormDef(FormDef formDef) {
		this.formDef = formDef;
	}

	
}
