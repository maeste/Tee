/*
 * Created on 5-set-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.interceptor;

import it.javalinux.tee.event.TestEvent;

public class AOPInterceptorTest /*extends TestCase*/ {

	public static void main(String[] args) {
		System.out.println("main");
		//junit.swingui.TestRunner.run(AOPInterceptorTest.class);
		AOPInterceptorTest me = new AOPInterceptorTest();
		me.testConstructorAdvice();
	}

	public void testConstructorAdvice() {
		System.out.println("metodo");
		TestEvent map = new TestEvent("aa",new Integer(1),2,new Float(5.5));
		map.pippo();
		//this.assertTrue(true);
	}

}
