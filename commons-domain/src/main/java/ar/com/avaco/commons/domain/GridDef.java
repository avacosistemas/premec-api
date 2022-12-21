/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author avaco
 *
 */
public class GridDef extends EntityDef{
	private List<ColumnDef> columnsDef;
	private Boolean sortAllColumns;
	private List<String> displayedColumns;
	
	public GridDef(EntityDef entityDef) {
//		this.setType("GRID");
		this.sortAllColumns = true;
		this.setColumnsByFields(entityDef.getFields());
	}
	private void setColumnsByFields(List<FieldDef> fields) {
		List<ColumnDef> columnsDef = new ArrayList<>();
		List<String> displayedColumns = new ArrayList<>(); 
		fields.forEach(f -> {
			ColumnDef columnDef = new ColumnDef();
			columnDef.setColumnDef(f.getKey());
			columnDef.setColumnNameKey(f.getLabelKey());
			columnsDef.add(columnDef);
			displayedColumns.add(f.getKey());
			
		});		
		this.columnsDef = columnsDef;
		this.displayedColumns = displayedColumns;
	}
	public Boolean getSortAllColumns() {
		return sortAllColumns;
	}
	public void setSortAllColumns(Boolean sortAllColumns) {
		this.sortAllColumns = sortAllColumns;
	}
	
	public List<ColumnDef> getColumnsDef() {
		return columnsDef;
	}
	public void setColumnsDef(List<ColumnDef> columnsDef) {
		this.columnsDef = columnsDef;
	}
	public List<String> getDisplayedColumns() {
		return displayedColumns;
	}
	public void setDisplayedColumns(List<String> displayedColumns) {
		this.displayedColumns = displayedColumns;
	}
}
