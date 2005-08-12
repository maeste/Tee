/*
 * Created on 11-ago-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import it.javalinux.tee.event.Event;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;

public class DLQController {
	private static Hashtable<String,String> env;
	
	static {
		env = new Hashtable<String,String>();
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
		env.put(javax.naming.Context.PROVIDER_URL, "localhost:1099");
	}

		
	
	public static void putEvent(Event inputEvent, String name) throws Exception {
		QueueSender sender = null;
		QueueSession queueSession = null;
		try {
			
			InitialContext ctx = new InitialContext(env);
			QueueConnectionFactory queueFactory = (QueueConnectionFactory) ctx.lookup("java:/XAConnectionFactory");
			QueueConnection queueConn = queueFactory.createQueueConnection();
			queueConn.start();
			
			queueSession = queueConn.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue)ctx.lookup("queue/" + name+"DLQ");
			sender = queueSession.createSender(queue);
			ObjectMessage message = queueSession.createObjectMessage(inputEvent);
			sender.send(message);
		} finally {
			try {
				sender.close();
			} catch(Exception e11) { }
			try {
				queueSession.close();
			} catch(Exception e1) { }
		}
	}
	
	public static Event getFirstEvent(String name) throws Exception {
		QueueReceiver receiver = null;
		QueueSession queueSession = null;
		try {
			
		
			InitialContext ctx = new InitialContext(env);
			QueueConnectionFactory queueFactory = (QueueConnectionFactory) ctx.lookup("java:/XAConnectionFactory");
			QueueConnection queueConn = queueFactory.createQueueConnection();
			queueConn.start();
			
			queueSession = queueConn.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue)ctx.lookup("queue/" + name+"DLQ");
			receiver = queueSession.createReceiver(queue);
			ObjectMessage message = (ObjectMessage) receiver.receiveNoWait();
			return (Event) message.getObject();
		} finally {
			try {
				receiver.close();
			} catch(Exception e11) { }
			try {
				queueSession.close();
			} catch(Exception e1) { }
		}
	}
	
	public static Event viewFirstEvent(String name) throws Exception {
		QueueBrowser browser = null;
		QueueSession queueSession = null;
		try {
		
			InitialContext ctx = new InitialContext(env);
			QueueConnectionFactory queueFactory = (QueueConnectionFactory) ctx.lookup("java:/XAConnectionFactory");
			QueueConnection queueConn = queueFactory.createQueueConnection();
			queueConn.start();
			
			queueSession = queueConn.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue)ctx.lookup("queue/" + name+"DLQ");
			browser = queueSession.createBrowser(queue);
			ObjectMessage message = (ObjectMessage) browser.getEnumeration().nextElement();
			return (Event) message.getObject();
		} finally {
			try {
				browser.close();
			} catch(Exception e11) { }
			try {
				queueSession.close();
			} catch(Exception e1) { }
		}
	
	}
	
	public static List<Event> getAllEvent(String name) throws Exception {
		QueueReceiver receiver = null;
		QueueSession queueSession = null;
		List<Event> listToReturn = new ArrayList<Event>();
		try {
			
		
			InitialContext ctx = new InitialContext(env);
			QueueConnectionFactory queueFactory = (QueueConnectionFactory) ctx.lookup("java:/XAConnectionFactory");
			QueueConnection queueConn = queueFactory.createQueueConnection();
			queueConn.start();
			
			queueSession = queueConn.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue)ctx.lookup("queue/" + name+"DLQ");
			receiver = queueSession.createReceiver(queue);
			Message message =  receiver.receiveNoWait();
			while ( message != null ) {
				ObjectMessage objMessage = (ObjectMessage) message;
				listToReturn.add((Event) objMessage.getObject());
				message = receiver.receiveNoWait();
				
			}
			return listToReturn;
		} finally {
			try {
				receiver.close();
			} catch(Exception e11) { }
			try {
				queueSession.close();
			} catch(Exception e1) { }
		}
	}
}
