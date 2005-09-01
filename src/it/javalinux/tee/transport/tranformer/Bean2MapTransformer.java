/*
 * Created on 16-ago-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.transport.tranformer;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.event.MapEvent;
import it.javalinux.tee.exception.TransformationException;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class Bean2MapTransformer implements TransformerInterface {

	public Event transform(Event inputEvent) throws IllegalArgumentException, TransformationException {
		MapEvent returnEvent =  new MapEvent();
		try {
			Map map = BeanUtils.describe(inputEvent);
			HashMap<String,Object> internalMap = new HashMap<String,Object>();
			internalMap.putAll(map);
			internalMap.remove("class");
			internalMap.put("EventName",inputEvent.getClass().getCanonicalName());
			returnEvent.setMap(internalMap);
			
		} catch (Exception e) {
			throw new TransformationException("unable to transform event:" + inputEvent.toString() + "caused by Exception:" + e.getMessage());
		}
		return returnEvent;
	}
	

}
