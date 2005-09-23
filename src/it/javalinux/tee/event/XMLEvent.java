/*
 * Created on 21-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.event;



public class XMLEvent implements Event {
	protected String xmlString;
	private Long interceptionTimeMillis;
	
	public Long getInterceptionTimeMillis() {
		return this.interceptionTimeMillis;
	}
	
	public void setInterceptionTimeMillis(Long interceptionTimeMillis) {
		this.interceptionTimeMillis = interceptionTimeMillis;
	}
	
	public String getXmlString() {
		return xmlString;
	}
	

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("Event intercepted at ");
		sb.append(this.getInterceptionTimeMillis());
		sb.append(": ");
		sb.append(this.getXmlString());
        return sb.toString();
	}


	public boolean equals(Object obj) {
		if (! (obj instanceof XMLEvent)) {
			return false;
		}
		if (this.xmlString == null) {
			if (((XMLEvent) obj).getXmlString() == null ) {
				return true;
			} else {
				return false;
			}
		}
		return this.xmlString.equals(((XMLEvent) obj).getXmlString());
	}


	public int hashCode() {
		return this.xmlString.hashCode();
	}
	
	


}
