/*
 * Created on 25-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import it.javalinux.tee.misc.ValidityErrorReporter;
import it.javalinux.tee.specification.EventSpec;
import it.javalinux.tee.specification.UnknownEventSpec;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.jboss.logging.Logger;
import org.jboss.system.server.ServerConfigLocator;

public class SpecificationDigester {


    private static final String SPEC_PCKG = "it.javalinux.tee.specification";
    private static final String XSD_PATH = "resource:jboss-tee.xsd";
	Map eventSpecMap;
	UnknownEventSpec unknownEventSpec;
	
	/**
	 * @param eventSpecMap
	 * @param unknownEventSpec
	 */
	public SpecificationDigester(Map eventSpecMap, UnknownEventSpec unknownEventSpec) {
		super();
		// TODO Auto-generated constructor stub
		this.eventSpecMap = eventSpecMap;
		this.unknownEventSpec = unknownEventSpec;
	}



	/**
	 * Reads and parses the specification file mapping events to handlers and transports
	 * 
	 */
	public void readSpecification(String teeName ) {
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
            SpecificationDigester o = (SpecificationDigester)digester.parse(specificationInputStream);
            
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
	
}
