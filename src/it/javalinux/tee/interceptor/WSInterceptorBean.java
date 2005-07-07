/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.interceptor;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.event.WSEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.logging.Logger;

/**
 * @author Alessio
 * 
 * @ejb.bean name="WSInterceptorBean"
 *           display-name="WSInterceptor Bean"
 *           type="Stateless"
 *           view-type="local"
 *           local-jndi-name="Tee.WSInterceptor"
 * @ejb.interface local-class="it.javalinux.tee.interceptor.WSInterceptor" 
 * @ejb.home local-class="it.javalinux.tee.interceptor.WSinterceptorHome"
 * @ejb.transaction type="Required"
 * @jboss-net.web-service urn="WSInterceptorBean"
 * 
 */
public class WSInterceptorBean implements Interceptor, SessionBean {

    
    private SessionContext sessionContext;
    
    /* (non-Javadoc)
     * @see it.javalinux.tee.interceptor.Interceptor#intercept(it.javalinux.tee.event.Event)
     */
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
    
    /**
	 *
	 * @param event
	 * @return
	 * @ejb.interface-method view-type="local"
	 * @jboss-net.web-method returnQName="interceptTemp"
	 */
    public void InterceptTemp(WSEvent wsEvent) {
        Logger.getLogger(this.getClass()).info("interceptTemp called!");
        
        this.intercept(wsEvent);
    }
    

    /* (non-Javadoc)
     * @see it.javalinux.tee.interceptor.Interceptor#logMyCall()
     */
    public void logMyCall() {
        // TODO Auto-generated method stub

    }
    
    public void ejbCreate() {

	}
	
	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}

	public void ejbRemove() throws EJBException, RemoteException {
	}
	
	public void setSessionContext(SessionContext arg0) 	throws EJBException, RemoteException {
		this.sessionContext = arg0;
	}
    
}
