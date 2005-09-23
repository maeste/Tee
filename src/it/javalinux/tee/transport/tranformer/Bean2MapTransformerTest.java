/*
 * Created on 16-ago-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.transport.tranformer;

import it.javalinux.tee.event.MapEvent;
import it.javalinux.tee.event.TestEvent;

import java.util.HashMap;

import junit.framework.TestCase;

public class Bean2MapTransformerTest extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(Bean2MapTransformerTest.class);
	}

	public void testTransform() {
		Long date = new Long(System.currentTimeMillis());
		TestEvent testEvent= new TestEvent();
		testEvent.setFooString("fooString");
		testEvent.setFooInteger(new Integer(1));
		testEvent.setFooInt(10);
		testEvent.setFooFloat(new Float(10.34));
		testEvent.setInterceptionTimeMillis(date);
		MapEvent mapEvent = new MapEvent();
		HashMap<String,Object> testMap = new HashMap<String,Object>();
		testMap.put("EventName","it.javalinux.tee.event.TestEvent");
		testMap.put("fooString","fooString");
		testMap.put("fooInteger",new Integer(1));
		testMap.put("fooInt","10");
		testMap.put("fooFloat","10.34");
		mapEvent.setMap(testMap);
		mapEvent.setInterceptionTimeMillis(date);
		
		Bean2MapTransformer transformer = new Bean2MapTransformer();
		try {
			MapEvent transformedEvent = (MapEvent) transformer.transform(testEvent);
			System.out.println(mapEvent);
			System.out.println(transformedEvent);
			//TODO bisogna verificare perchè equals direttamente sulle mappe fallisce
			assertTrue(mapEvent.toString().equals(transformedEvent.toString()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
