package ar.com.avaco.arc.core.domain.filter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilter {

	private Integer rows;

	private Integer first;

	private Boolean asc;

	private String idx;
	
	private Boolean distinctRootEntity;

	public List<FilterData> getFilterDatas() {
		return new ArrayList<FilterData>();
	}

	public List<List<FilterData>> getOrFilterDatas(){
		return new ArrayList<List<FilterData>>();
	}
	
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Boolean isAsc() {
		return asc;
	}

	public String getSidx() {
		return idx;
	}

	public Boolean getAsc() {
		return asc;
	}

	public void setAsc(Boolean asc) {
		this.asc = asc;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Boolean getDistinctRootEntity() {
		return distinctRootEntity;
	}

	public void setDistinctRootEntity(Boolean distinctRootEntity) {
		this.distinctRootEntity = distinctRootEntity;
	}
	
}