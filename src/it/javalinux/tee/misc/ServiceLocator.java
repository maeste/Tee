/*
 * Created on 22-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.javalinux.tee.misc;

import java.util.Properties;

import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.logging.Logger;

/**
 * @author Alessio
 *
 */
public class ServiceLocator {
    
    private static ServiceLocator me = null;
    private InitialContext ctx = null;
    
    
    private ServiceLocator() throws Exception {
        Properties prop = new Properties();
        prop.put( "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" );
        prop.put( "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" );
        prop.put( "java.naming.provider.url", "jnp://localhost:1099");
        ctx = new InitialContext(prop);
    }
    
    
    public synchronized static ServiceLocator getInstance() throws Exception {
        if ( me == null ) {
            me = new ServiceLocator();
        }
        return me;
    }
    
    
    public void callMBean(String jndiName, String method, Object[] parameters, String[] signature) throws Exception {
        RMIAdaptor rmiserver = null;
        Logger.getLogger(ServiceLocator.class).debug("Looking up RMI adaptor...");
        rmiserver = (RMIAdaptor) ctx.lookup("jmx/invoker/RMIAdaptor");
        if( rmiserver == null ) Logger.getLogger(ServiceLocator.class).debug( "RMIAdaptor is null");
        ObjectName oName = new ObjectName(jndiName);
        StringBuffer sb = new StringBuffer("Invoking method ");
        sb.append(method);
        sb.append("(");
        for (int i=0; i<signature.length; i++) {
            sb.append(signature[i]);
            sb.append(" par");
            sb.append(i);
            if (i+1<signature.length) {
                sb.append(", ");
            }
        }
        sb.append(") on: ");
        sb.append(jndiName);
        Logger.getLogger(ServiceLocator.class).debug(sb.toString());
        rmiserver.invoke(oName,method,parameters,signature);
    }
    
}
