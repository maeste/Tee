/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.event;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Alessio
 * @jboss-net.xml-schema urn="tee:WSEvent"
 */
public class WSEvent implements Event, Serializable {
    
    public WSEvent() {
        
    }

    /* (non-Javadoc)
     * @see it.javalinux.tee.event.Event#getInterceptTime()
     */
    public Date getInterceptTime() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see it.javalinux.tee.event.Event#getInterceptorName()
     */
    public String getInterceptorName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String toString() {
        return "I'm a WSEvent :-)";
    }
    
    
}
