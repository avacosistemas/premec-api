package ar.com.avaco.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class PostLoginSAPDTO {

	private String CompanyDB;
	private String UserName;
	private String Password;

	public String getCompanyDB() {
		return CompanyDB;
	}

	public void setCompanyDB(String companyDB) {
		CompanyDB = companyDB;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public Map<String, Object> getAsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CompanyDB", CompanyDB);
		map.put("UserName", UserName);
		map.put("Password", Password);
		return map;
	}

}
