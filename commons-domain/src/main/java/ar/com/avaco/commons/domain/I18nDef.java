/**
 * 
 */
package ar.com.avaco.commons.domain;

import java.util.Map;

/**
 * @author avaco
 *
 */
public class I18nDef {
	private String name;
	private String lang;
	private Map<String, String> words;
	
	public I18nDef(String name, Map<String, String> keyValue) {
		this.name = name; 
		this.setLang("es");
		this.words = keyValue;
//		this.setMapWords(keys);
	}
	
//	private void setMapWords(List<String> keys) {	
//		Map<String, String> words = new LinkedHashMap<String, String>();
//		keys.forEach(k -> {
//			words.put(k.toLowerCase(), k.toLowerCase());
//		});
//		this.words = words;
//	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Map<String, String> getWords() {
		return words;
	}
	public void setWords(Map<String, String> words) {
		this.words = words;
	}
}
