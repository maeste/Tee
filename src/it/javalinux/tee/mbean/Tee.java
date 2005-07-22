/*
 * Created on 7-giu-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.misc.TeeHelper;
import it.javalinux.tee.misc.ValidityErrorReporter;
import it.javalinux.tee.specification.EventSpec;
import it.javalinux.tee.specification.HandlerSpec;
import it.javalinux.tee.specification.TransportSpec;
import it.javalinux.tee.specification.UnknownEventSpec;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.digester.Digester;
import org.jboss.aspects.asynch.Asynchronous;
import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.server.ServerConfigLocator;

/**
 * @author oracle
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Tee extends ServiceMBeanSupport implements TeeMBean  {
    

    private static final String SPEC_PCKG = "it.javalinux.tee.specification";
    private static final String XSD_PATH = "resource:jboss-tee.xsd";
    
	private String teeName;
	private Map eventSpecMap = new HashMap();
	private UnknownEventSpec unknownEventSpec;
	
	
	
	/* (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#createService()
	 */
	protected void createService() throws Exception {
		super.createService();
		Logger.getLogger(this.getClass()).debug("createService");
	}
	
	
	/* (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#destroyService()
	 */
	protected void destroyService() throws Exception {
		super.destroyService();
		Logger.getLogger(this.getClass()).debug("destroyService");
	}
	
	
	/* (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#startService()
	 */
	protected void startService() throws Exception {
		super.startService();
		Logger.getLogger(this.getClass()).debug("startService");
		this.readSpecification();
	}
	
	
	/* (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#stopService()
	 */
	protected void stopService() throws Exception {
		super.stopService();
		Logger.getLogger(this.getClass()).debug("stopService");
	}

	
	
	/**
	 * Reads and parses the specification file mapping events to handlers and transports
	 * 
	 */
	private void readSpecification() {
		String xmlPath = ServerConfigLocator.locate().getServerTempDeployDir().getAbsolutePath()+
			"/"+teeName+"-dep.xml";
		
	    if (!eventSpecMap.isEmpty()) {
	        Logger.getLogger(this.getClass()).debug("Clearing eventSpecMap!");
	        eventSpecMap.clear();
	    }
	    Digester digester =  new Digester();
	    try {
		    digester.register("http://www.w3.org/2001/XMLSchema-instance", XSD_PATH);
		    digester.setSchema(XSD_PATH);
	        digester.setValidating(true);
	        digester.setErrorHandler(new ValidityErrorReporter()); //set my error handler, otherwise Digester ignores validation exceptions
	        digester.push(this);
		    //UnknownEvent
	        digester.addObjectCreate("Tee/UnknownEvent", SPEC_PCKG+".UnknownEventSpec");
	        digester.addSetNext("Tee/UnknownEvent","setUnknownEventSpec", SPEC_PCKG+".UnknownEventSpec");
	        //Event
	        digester.addObjectCreate("Tee/Event", SPEC_PCKG+".EventSpec");
	        digester.addCallMethod("Tee/Event/EventClass", "setEventClass", 0);
	        digester.addSetNext("Tee/Event","addEventSpec", SPEC_PCKG+".EventSpec");
	        //Handler
	        digester.addObjectCreate("*/Handler", SPEC_PCKG+".HandlerSpec");
	        digester.addSetNext("*/Handler", "addHandler", SPEC_PCKG+".HandlerSpec");
	        digester.addCallMethod("*/Handler/HandlerClass", "setHandlerClass", 0);
	        String[] logMeType={"java.lang.Boolean"};
	        digester.addCallMethod("*/Handler/LogMe", "setLogMe", 0, logMeType);
	        //HibernateTransport
	        digester.addObjectCreate("*/HibernateTransport", SPEC_PCKG+".HibernateTransportSpec");
	        digester.addSetNext("*/HibernateTransport", "addTransport", SPEC_PCKG+".TransportSpec");
	        digester.addCallMethod("*/HibernateTransport/HibernableEventClass", "setHibernableEventClass", 0);
	        //CustomTransport
	        digester.addObjectCreate("*/CustomTransport", SPEC_PCKG+".CustomTransportSpec");
	        digester.addSetNext("*/CustomTransport", "addTransport", SPEC_PCKG+".TransportSpec");
	        digester.addCallMethod("*/CustomTransport/TransportClass", "setTransportClass", 0);
	        digester.addObjectCreate("*/CustomTransport/Attribute", SPEC_PCKG+".AttributeSpec");
	        digester.addSetNext("*/CustomTransport/Attribute", "addAttribute", SPEC_PCKG+".AttributeSpec");
	        digester.addCallMethod("*/CustomTransport/Attribute/Name", "setName", 0);
	        digester.addCallMethod("*/CustomTransport/Attribute/Type", "setType", 0);
	        digester.addCallMethod("*/CustomTransport/Attribute/Value", "setValue", 0);
	        //Log4JTransport
	        digester.addObjectCreate("*/Log4jTransport", SPEC_PCKG+".Log4jTransportSpec");
	        digester.addSetNext("*/Log4jTransport", "addTransport", SPEC_PCKG+".TransportSpec");
	        digester.addCallMethod("*/Log4jTransport/DebugLevel", "setDebugLevel", 0);
	        digester.addCallMethod("*/Log4jTransport/Prefix/EventClassName", "setEventClassNamePrefixType");
	        digester.addCallMethod("*/Log4jTransport/Prefix/TransportClassName", "setTransportClassNamePrefixType");
	        digester.addCallMethod("*/Log4jTransport/Prefix/Custom", "setCustomPrefixType");
	        digester.addCallMethod("*/Log4jTransport/Prefix/Custom", "setPrefix", 0);
            //TeeTransport
            digester.addObjectCreate("*/TeeTransport", SPEC_PCKG+".TeeTransportSpec");
            digester.addSetNext("*/TeeTransport", "addTransport", SPEC_PCKG+".TransportSpec");
            digester.addCallMethod("*/TeeTransport/TeeJndiName", "setTeeJndiName", 0);
            //XML2BeanTransformer
            digester.addObjectCreate("*/XML2BeanTransformer", SPEC_PCKG+".XML2BeanTransformerSpec");
            digester.addSetNext("*/XML2BeanTransformer", "setTransformer", SPEC_PCKG+".TransformerSpec");
            //CustomTransformer
            digester.addObjectCreate("*/CustomTransformer", SPEC_PCKG+".CustomTransformerSpec");
            digester.addSetNext("*/CustomTransformer", "setTransformer", SPEC_PCKG+".TransformerSpec");
            digester.addCallMethod("*/CustomTransformer/CustomTranspormerClass", "setCustomTransformerClass", 0);
	        
            InputStream specificationInputStream = null;
            try {
                URL url = new URL(xmlPath);
                specificationInputStream = url.openStream();
            } catch (MalformedURLException mue) {
                specificationInputStream = new BufferedInputStream(new FileInputStream(xmlPath));
            }
            Logger.getLogger(this.getClass()).debug(new StringBuffer("Parsing file ").append(xmlPath).toString());
            Tee o = (Tee)digester.parse(specificationInputStream);
            
            /** Prova da qui in avanti **/
            for (Iterator i = eventSpecMap.keySet().iterator(); i.hasNext(); ) {
                String key = (String)i.next();
                Logger.getLogger(this.getClass()).info("** key = "+key+" payload = "+eventSpecMap.get(key));
            }
            Logger.getLogger(this.getClass()).info("Evento unknown: "+unknownEventSpec);
            /** Fine prova**/
            
            specificationInputStream.close();
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).info("An error occurred while parsing specification file "+teeName);
            e.printStackTrace();
        }
        
	}
	
	/**
	 * Adds an event mapping specification to the events' map using the event class as key.
	 * 
	 * @param eventSpec
	 */
	public void addEventSpec(EventSpec eventSpec) {
        eventSpecMap.put(eventSpec.getEventClass(), eventSpec);
    }
	
	/**
	 * Remove an event mapping specification given the event class name.
	 * 
	 * @param eventClassName
	 * @return The event mapping specification previously associated with the key.
	 */
	public EventSpec removeEventSpec(String eventClassName) {
        return (EventSpec)eventSpecMap.remove(eventClassName);
    }
	
	
	/**
	 * Sets the unknown event mapping specification
	 * 
	 * @param unknownEventSpec
	 */
    public void setUnknownEventSpec(UnknownEventSpec unknownEventSpec) {
        this.unknownEventSpec = unknownEventSpec;
    }
	
	
    /**
     * @param event
     * 
     */
	@Asynchronous
	public void process(Event event) {
		UserTransaction userTransaction = null;
		try {
			Properties prop = new Properties();
			prop.put("java.naming.factory.initial",  "org.jnp.interfaces.NamingContextFactory");
			prop.put("java.naming.factory.url.pkgs", 	"org.jboss.naming:org.jnp.interfaces");
			prop.put("java.naming.provider.url", "jnp://localhost:1099");
			InitialContext ctx = new InitialContext(prop);
			userTransaction = (UserTransaction) ctx.lookup("UserTransaction");
			userTransaction.begin();
		} catch (Exception e) {
			Logger.getLogger("Transaction begin failed");
		}
		
		try {
			
		    Logger.getLogger(this.getClass()).info("Processing event of class "+event.getClass().getName());
			//get handler and/or transport right for this event 
			// and invoke them passing event and log option
		    TeeHelper helper = new TeeHelper();
		    Object obj = eventSpecMap.get(event.getClass().getName());
	        if (obj!=null) {
	            EventSpec eventSpec = (EventSpec)obj;
                if (eventSpec.getTransformer()!=null) {
                    event = helper.transformEvent(event, eventSpec.getTransformer());
                }
	            if (eventSpec.getHandlerSpecList().size()>0 || eventSpec.getTransportSpecList().size()>0) {
	                for (Iterator it = eventSpec.getHandlerSpecList().iterator(); it.hasNext(); ) {
	                    helper.processWithHandler(event, (HandlerSpec)it.next());
	                }
	                for (Iterator it = eventSpec.getTransportSpecList().iterator(); it.hasNext(); ) {
	                    helper.processWithTransport(event, (TransportSpec)it.next());
	                }
	            } else {
	                Logger.getLogger(this.getClass()).info("No handler or transport found for events of class "+event.getClass());
	            }
	        } else {
	            //unknown event
                if (unknownEventSpec.getTransformer()!=null) {
                    event = helper.transformEvent(event, unknownEventSpec.getTransformer());
                }
	            if (unknownEventSpec.getHandlerSpecList().size()>0 || unknownEventSpec.getTransportSpecList().size()>0) {
	                for (Iterator it = unknownEventSpec.getHandlerSpecList().iterator(); it.hasNext(); ) {
	                    helper.processWithHandler(event, (HandlerSpec)it.next());
	                }
	                for (Iterator it = unknownEventSpec.getTransportSpecList().iterator(); it.hasNext(); ) {
	                    helper.processWithTransport(event, (TransportSpec)it.next());
	                }
	            } else {
	                Logger.getLogger(this.getClass()).info("No handler or transport found for events of class "+event.getClass());
	            }
	        }
	    } catch (Exception e) {
	        Logger.getLogger(this.getClass()).error("Error processing event of class "+event.getClass());
			StringWriter sw = new StringWriter();
    		e.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
			try {
				userTransaction.setRollbackOnly();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	
			try {
				userTransaction.commit();
			} catch (Exception e) {
				
				// TODO Qui bisogna salvare da qualche parte l'evento.
				e.printStackTrace();
			} 
		
	}
	
	
	/**
	 * @return Returns the TeeName.
	 */
	public String getTeeName() {
		return teeName;
	}
	
	
	/**
	 * @param teeName The TeeName to set.
	 */
	public void setTeeName(String teeName) {
		this.teeName = teeName;
	}
	
}
