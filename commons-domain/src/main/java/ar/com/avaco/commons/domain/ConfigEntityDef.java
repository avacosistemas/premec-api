/**
 * 
 */
package ar.com.avaco.commons.domain;

/**
 * @author avaco
 *
 */
public class ConfigEntityDef extends EntityDef{
	private String ws;
	private String route;
	// Configuracion basica de titulo y nombre de menu
	private String pageTitle;
	private String navTitle;
	
	public String getWs() {
		return ws;
	}
	public void setWs(String ws) {
		this.ws = ws;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getNavTitle() {
		return navTitle;
	}
	public void setNavTitle(String navTitle) {
		this.navTitle = navTitle;
	}
	
}
