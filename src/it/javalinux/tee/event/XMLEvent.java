/*
 * Created on 21-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.event;


public class XMLEvent implements Event {
	protected String xmlString;

	public String getXmlString() {
		return xmlString;
	}
	

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}


	public String toString() {
		return this.getXmlString();
	}
	
	


}
