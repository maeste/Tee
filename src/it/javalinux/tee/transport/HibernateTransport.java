/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.transport;

import it.javalinux.tee.event.Event;

/**
 * @author Alessio
 *
 */
public class HibernateTransport implements Transport {
    
    private String hibernableEventClass;
    
    public String getHibernableEventClass() {
        return hibernableEventClass;
    }
    public void setHibernableEventClass(String hibernableEventClass) {
        this.hibernableEventClass = hibernableEventClass;
    }
    
    /* (non-Javadoc)
     * @see it.javalinux.tee.transport.Transport#process(it.javalinux.tee.event.Event)
     */
    public void process(Event event) {
        // TODO Auto-generated method stub

    }

}
