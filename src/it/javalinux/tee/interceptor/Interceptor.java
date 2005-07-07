/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.interceptor;

import it.javalinux.tee.event.Event;

/**
 * @author Alessio
 *
 */
public interface Interceptor {
    
    public void intercept(Event event);
    
    public void logMyCall();
    
}
