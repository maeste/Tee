/*
 * Created on 21-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.transport.tranformer;

import it.javalinux.tee.event.TestEvent;
import it.javalinux.tee.event.XMLEvent;
import junit.framework.TestCase;

public class XML2BeanTransformerTest extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(XML2BeanTransformerTest.class);
	}

	public void testTransform() {
		try {
			XMLEvent xmlEvent = new XMLEvent();
		
			xmlEvent.setXmlString("<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
					"<XmlEvent>" +
					"<EventName>it.javalinux.tee.event.TestEvent</EventName>" +
					"<fooString>fooString</fooString>" +
					"<fooInteger>1</fooInteger>" +
					"<fooInt>10</fooInt>" +
					"<fooFloat>10.34</fooFloat>" +
					"</XmlEvent>");
			XML2BeanTransformer transformer = new XML2BeanTransformer();
			TestEvent testEvent = (TestEvent) transformer.transform(xmlEvent);
			assertEquals(testEvent.getFooString(),"fooString");
			assertEquals(testEvent.getFooInteger(),new Integer(1));
			assertEquals(testEvent.getFooInt(),10);
			assertEquals(testEvent.getFooFloat(),new Float(10.34));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

}
