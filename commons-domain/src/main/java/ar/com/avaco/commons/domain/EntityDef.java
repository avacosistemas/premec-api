/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.util.List;

/**
 * @author avaco
 *
 */
public class EntityDef {
	private String key;
	private String type;
	private List<FieldDef> fields;


	public List<FieldDef> getFields() {
		return fields;
	}

	public void setFields(List<FieldDef> fields) {
		this.fields = fields;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
