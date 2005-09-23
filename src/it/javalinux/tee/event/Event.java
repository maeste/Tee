/*
 * Created on 7-giu-2005
 *
 */
package it.javalinux.tee.event;

import java.io.Serializable;


/**
 * @author oracle
 *
 */
public interface Event extends Serializable {
	
	public Long getInterceptionTimeMillis();
	
	public void setInterceptionTimeMillis(Long interceptionTimeMillis);
	
}
