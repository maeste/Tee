/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.event;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Alessio
 * @jboss-net.xml-schema urn="tee:MapEvent"
 */
public class MapEvent implements Event, Serializable {
    
    private HashMap<String,Object> map = new HashMap<String,Object>();
	private Long interceptionTimeMillis = null;
    
    public MapEvent() {
        
    }
	
	public Long getInterceptionTimeMillis() {
		return this.interceptionTimeMillis;
	}
	
	public void setInterceptionTimeMillis(Long interceptionTimeMillis) {
		this.interceptionTimeMillis = interceptionTimeMillis;
	}
    
    public String toString() {
		StringBuffer sb = new StringBuffer("Event intercepted at ");
		sb.append(this.getInterceptionTimeMillis());
		sb.append(": ");
		sb.append(map.toString());
        return sb.toString();
    }
    
    
    public HashMap<String,Object> getMap() {
        return map;
    }
    public void setMap(HashMap<String,Object> map) {
        this.map = map;
    }


	public boolean equals(Object obj) {
		if (!(obj instanceof MapEvent)) {
			return false;
		}
		return this.getMap().equals(((MapEvent) obj).getMap());
	}


	public int hashCode() {
		return this.getMap().hashCode();
	}
	
	
}
