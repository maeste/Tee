/*
 * Created on 16-ago-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.transport.tranformer;



import it.javalinux.tee.event.Event;
import it.javalinux.tee.event.XMLEvent;
import it.javalinux.tee.exception.TransformationException;

import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class Bean2XMLTransformer implements TransformerInterface {

	public Event transform(Event inputEvent) throws IllegalArgumentException, TransformationException {
		XMLEvent returnEvent = new XMLEvent();
		StringWriter strWriter = new StringWriter();
		try {
			strWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			strWriter.write("<XmlEvent>");
			strWriter.write("<EventName>"+ inputEvent.getClass().getCanonicalName()+"</EventName>");
			Map propertyMap = BeanUtils.describe(inputEvent);
			for (Object propertyName : propertyMap.keySet() ) {
				if (!"class".equals(propertyName) && !"interceptionTimeMillis".equals(propertyName) &&
						propertyMap.get(propertyName)!=null) {
					strWriter.write("<" + propertyName + ">");
					strWriter.write(propertyMap.get(propertyName).toString());
					strWriter.write("</" + propertyName + ">");
				}
				
			}
			try {
				returnEvent.setInterceptionTimeMillis(Long.parseLong((String)propertyMap.get("interceptionTimeMillis")));
			} catch (Exception e) {}
			strWriter.write("</XmlEvent>");
		}catch (Exception e) {
			e.printStackTrace();
			throw new TransformationException("unable to transform event:" + inputEvent.toString() + " caused by Exception:" + e.getMessage());
		}
		returnEvent.setXmlString(strWriter.toString());
		return returnEvent;

		
	}

}
