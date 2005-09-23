/*
 * Created on 16-ago-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.transport.tranformer;

import it.javalinux.tee.event.TestEvent;
import it.javalinux.tee.event.XMLEvent;
import junit.framework.TestCase;

public class Bean2XMLTransformerTest extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(Bean2XMLTransformerTest.class);
	}

	public void testTransform() {
		try {
			XMLEvent xmlEvent = new XMLEvent();
			Long date= new Long(System.currentTimeMillis());
		
			xmlEvent.setXmlString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<XmlEvent>" +
					"<EventName>it.javalinux.tee.event.TestEvent</EventName>" +
					"<fooString>fooString</fooString>" +
					"<fooInteger>1</fooInteger>" +
					"<fooFloat>10.34</fooFloat>" +
					"<fooInt>10</fooInt>" +
					"</XmlEvent>");
			xmlEvent.setInterceptionTimeMillis(date);
			TestEvent testEvent= new TestEvent();
			testEvent.setFooString("fooString");
			testEvent.setInterceptionTimeMillis(date);
			//testEvent.setFooInteger(new Integer(1));
			//testEvent.setFooInt(10);
			//testEvent.setFooFloat(new Float(10.34));
			Bean2XMLTransformer transformer = new Bean2XMLTransformer();
			XMLEvent transformedEvent = (XMLEvent) transformer.transform(testEvent);
			System.out.println("xmlEvent:         "+xmlEvent);
			System.out.println("transformedEvent: "+transformedEvent);
			assertTrue(xmlEvent.equals(transformedEvent));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
