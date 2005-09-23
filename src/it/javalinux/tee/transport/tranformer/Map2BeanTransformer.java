/*
 * Created on 21-lug-2005
 *
 */
package it.javalinux.tee.transport.tranformer;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.event.MapEvent;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;

public class Map2BeanTransformer implements TransformerInterface {
	
	/* (non-Javadoc)
	 * @see it.javalinux.tee.transport.tranformer.TransformerInterface#transform(it.javalinux.tee.event.Event)
	 */
	public Event transform(Event inputEvent) throws IllegalArgumentException {
		if (!(inputEvent instanceof MapEvent) ) {
			throw new IllegalArgumentException("Cant apply Map2BeanTransformer to an event that is not an MAPEvent");
		}
		
		HashMap<String,Object> mapOfEvent = ((MapEvent) inputEvent).getMap();
		Object eventName = mapOfEvent.get("EventName");
		if (eventName == null || eventName.equals("") || ! (eventName instanceof String) ) {
			throw new IllegalArgumentException("EventName not present in Map as String containing complete class name");
		}
		Event bean;
		try {
			bean = (Event) Thread.currentThread().getContextClassLoader().loadClass((String) eventName).newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossible to instanciate EventName bean:" + eventName);
		}
		
		for (Iterator<String> iter = mapOfEvent.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			if ("EventName".equalsIgnoreCase(key)) {
				continue;
			}
			try {
				BeanUtils.copyProperty(bean,key,mapOfEvent.get(key));
			} catch (Exception e) {
				throw new IllegalArgumentException("Check your xml.I Can't set this property:" + key);
			}
		}
		bean.setInterceptionTimeMillis(inputEvent.getInterceptionTimeMillis());
		return bean;
	}

}
