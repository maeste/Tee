/*
 * Created on 21-giu-2005
 *
 */
package it.javalinux.tee.interceptor;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.event.XMLEvent;
import it.javalinux.tee.misc.ServiceLocator;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

import oracle.jms.AQjmsAdtMessage;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.logging.Logger;

/**
 * @author Alessio
 *
 * @ejb.bean name="OAQInterceptorMessageBean"
 *           display-name="OAQInterceptorMessageBean"
 *           type="Stateless"
 *           view-type="local"
 *           destination-type="javax.jms.Queue"
 *           acknowledge-mode="Auto-acknowledge"
 * @ejb.transaction type="Required"
 * @jboss.destination-jndi-name name="queue/DLTeeQueue"
 * @jboss.container-configuration name="OAQ DL Message Driven Bean"
 * 
*/

public class OAQInterceptorMessageBean implements MessageDrivenBean, MessageListener, Interceptor {
    
    private MessageDrivenContext context;
    
    /**
     * Equeues a message from the Tee's queue, then calls the Tee's MBean passing it.
     * 
     */
	public void onMessage(Message arg0) {
	    try {
	        Logger.getLogger(this.getClass()).debug("Message received...");
	        Event event = (Event)((AQjmsAdtMessage)arg0).getAdtPayload();
	        Logger.getLogger(this.getClass()).debug(event.getClass());
			if (event instanceof XMLEvent) {
				XMLEvent xmlEvent = (XMLEvent)event;
				xmlEvent.setXmlString(xmlEvent.getXmlString());
			}
			this.intercept(event);
	    } catch (Exception e) {
	        Logger.getLogger(this.getClass()).error("Error while enqueueing!");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(this.getClass()).error(sw.toString());
		}
	}
	
	
	public void intercept(Event event) {
	    RMIAdaptor rmiserver = null;
        try {
            String teeName = (String) context.lookup("java:comp/env/teeName");
            String jndiName = "it.javalinux:service="+teeName;
            Object[] parArray = {event};
            String[] signArray = {"it.javalinux.tee.event.Event"};
            ServiceLocator.getInstance().callMBean(jndiName,"process",parArray, signArray);
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("Error calling Tee service!");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(this.getClass()).error(sw.toString());
        }
	}
	
	
	public void ejbCreate() {
    }

    public void ejbRemove(){
    }

    public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException {
        this.context = arg0;
	}
}
