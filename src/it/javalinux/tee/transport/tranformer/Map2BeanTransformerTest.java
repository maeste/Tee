/*
 * Created on 21-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.transport.tranformer;

import it.javalinux.tee.event.MapEvent;
import it.javalinux.tee.event.TestEvent;

import java.util.HashMap;

import junit.framework.TestCase;

public class Map2BeanTransformerTest extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(Map2BeanTransformerTest.class);
	}

	public void testTransform() {
		try {
			Long date = new Long(System.currentTimeMillis());
			MapEvent mapEvent = new MapEvent();
			HashMap<String,Object> testMap = new HashMap<String,Object>();
			testMap.put("EventName","it.javalinux.tee.event.TestEvent");
			testMap.put("fooString","fooString");
			testMap.put("fooInteger",new Integer(1));
			testMap.put("fooInt","10");
			testMap.put("fooFloat","10.34");
			mapEvent.setInterceptionTimeMillis(date);
			mapEvent.setMap(testMap);
			Map2BeanTransformer transformer = new Map2BeanTransformer();
			TestEvent testEvent = (TestEvent) transformer.transform(mapEvent);
			System.out.println(testEvent);
			assertEquals(testEvent.getFooString(),"fooString");
			assertEquals(testEvent.getFooInteger(),new Integer(1));
			assertEquals(testEvent.getFooInt(),10);
			assertEquals(testEvent.getFooFloat(),new Float(10.34));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

}
