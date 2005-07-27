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
    
    public MapEvent() {
        
    }

    
    public String toString() {
        return "I'm a MapEvent :-)";
    }
    
    
    public HashMap<String,Object> getMap() {
        return map;
    }
    public void setMap(HashMap<String,Object> map) {
        this.map = map;
    }
}
