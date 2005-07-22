/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.interceptor;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.event.MapEvent;
import it.javalinux.tee.misc.ServiceLocator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

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
 * @ejb.home local-class="it.javalinux.tee.interceptor.WSInterceptorHome"
 * @ejb.transaction type="Required"
 * @jboss-net.web-service urn="WSInterceptorBean"
 * 
 * TUTTA QUESTA ROBA SOPRA IN REALTA' NON VIENE USATA...
 *  
 */
public class WSInterceptorBean implements Interceptor, SessionBean {

    
    private SessionContext sessionContext;
    
    /* (non-Javadoc)
     * @see it.javalinux.tee.interceptor.Interceptor#intercept(it.javalinux.tee.event.Event)
     */
    public void intercept(Event event) {
        try {
            String teeName = (String) sessionContext.lookup("java:comp/env/teeName");
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
    
    /**
	 *
	 * @param event
	 * @return
	 * @ejb.interface-method view-type="local"
	 * @jboss-net.web-method returnQName="interceptMapEvent"
	 */
    public void InterceptMapEvent(MapEvent mapEvent) {
        Logger.getLogger(this.getClass()).debug("interceptMapEvent called!");
        this.intercept(mapEvent);
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
