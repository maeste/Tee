/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.handler;

import it.javalinux.tee.event.Event;

/**
 * @author Alessio
 *
 */
public interface Handler {
    
    public void process(Event event, boolean logMe);
    
    public void logMyCall();
    
}
