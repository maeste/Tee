/*
 * Created on 21-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.transport.tranformer;


import it.javalinux.tee.event.Event;
import it.javalinux.tee.exception.TransformationException;



public interface TransformerInterface {

	public Event transform(Event inputEvent) throws IllegalArgumentException, TransformationException;

}