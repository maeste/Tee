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
public interface Transport {
    
    public void process(Event event);
    
}
