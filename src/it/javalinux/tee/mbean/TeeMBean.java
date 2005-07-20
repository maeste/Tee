/*
 * Created on 7-giu-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import it.javalinux.tee.event.Event;

import org.jboss.system.ServiceMBean;

/**
 * @author oracle
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface TeeMBean extends ServiceMBean{
    
	public void process(Event event);
	
	/**
	 * @return Returns the specificationURLString.
	 */
	public String getSpecificationURLString();
	/**
	 * @param specificationURLString The specificationURLString to set.
	 */
	public void setSpecificationURLString(String specificationURLString);

	
	
}