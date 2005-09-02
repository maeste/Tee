/*
 * Created on 21-ago-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee;

import org.jboss.aop.Aspect;
import org.jboss.aspects.asynch.AsynchExecutor;

@AsynchExecutor (value=it.javalinux.tee.MyExecutor.class) 
public class TestThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(TestThread.class.getAnnotations().length);
		TestThread test = new TestThread();
		for (int i = 0; i < 15; i++) {
			System.out.println("starting thread #" + i);
			test.testThreadExec(i);
		}
	}
	
	@org.jboss.aspects.asynch.Asynchronous
	public int testThreadExec(int input) {
		System.out.println("entering in thread #" + input);
		System.out.println("named" + Thread.currentThread().getName());
		try {
			//Thread.currentThread().notifyAll();
			Thread.sleep(3000);
			//Thread.currentThread().notifyAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("quitting thread #"+input);
		
		if (input==14) System.exit(0);
		return input;
	}

}
