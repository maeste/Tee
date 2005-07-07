/*
 * Created on 21-giu-2005
 *
 */
package it.javalinux.tee.interceptor;

import it.javalinux.tee.event.Event;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.management.ObjectName;
import javax.naming.InitialContext;

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
    
    private MessageDrivenContext ctx;
    
    /**
     * Equeues a message from the Tee's queue, then calls the Tee's MBean passing it.
     * 
     */
	public void onMessage(Message arg0) {
	    try {
	        Logger.getLogger(this.getClass()).info("Intercettato evento!!");
	        Event event = (Event)((AQjmsAdtMessage)arg0).getAdtPayload();
	        Logger.getLogger(this.getClass()).info(event.getClass());
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
            Properties prop = new Properties();
            prop.put( "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" );
            prop.put( "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" );
            prop.put( "java.naming.provider.url", "jnp://localhost:1099");
            InitialContext ctx = new InitialContext(prop);
            Logger.getLogger(this.getClass()).debug("Looking up RMI adaptor...");
	        rmiserver = (RMIAdaptor) ctx.lookup("jmx/invoker/RMIAdaptor");
	        if( rmiserver == null ) Logger.getLogger(this.getClass()).debug( "RMIAdaptor is null");
            ObjectName teeOName = new ObjectName("it.javalinux:service=Tee");
		    Object[] parArray = {event};
            String[] signArray = {"it.javalinux.tee.event.Event"};
            Logger.getLogger(this.getClass()).debug("Invoking service...");
		    rmiserver.invoke(teeOName,"process",parArray,signArray);
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("Error calling Tee service!");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(this.getClass()).error(sw.toString());
        }
	}
	
	
	public void logMyCall() {
	    //TODO!!
	}
	
	public void ejbCreate() {
    }

    public void ejbRemove(){
    }

    public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException {
        this.ctx = arg0;
	}
}
