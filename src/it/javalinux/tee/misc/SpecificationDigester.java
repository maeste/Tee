/*
 * Created on 25-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.misc;

import it.javalinux.tee.specification.EventSpec;
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
import java.util.Set;

import org.apache.commons.digester.Digester;
import org.jboss.logging.Logger;
import org.jboss.system.server.ServerConfigLocator;

public class SpecificationDigester {


    private static final String SPEC_PCKG = "it.javalinux.tee.specification";
    private static final String XSD_PATH = "resource:tee-events.xsd";
	private Map<String,EventSpec> eventSpecMap = new HashMap<String,EventSpec>();
	private UnknownEventSpec unknownEventSpec;
	
	/**
	 */
	public SpecificationDigester() {
		super();
	}



	/**
	 * Reads and parses the specification file mapping events to handlers and transports
	 * 
	 */
	public void readSpecification(String teeName ) throws Exception {
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
			//Transport
			digester.addObjectCreate("*/Transport", SPEC_PCKG+".TransportSpec");
			digester.addSetNext("*/Transport", "addTransport", SPEC_PCKG+".TransportSpec");
			
	        //HibernateTransport
	        digester.addObjectCreate("*/Transport/HibernateTransport", SPEC_PCKG+".HibernateTransportSpec");
	        digester.addSetNext("*/Transport/HibernateTransport", "setInnerTransport", SPEC_PCKG+".TransportSpecInterface");
	        digester.addCallMethod("*/Transport/HibernateTransport/HibernableEventClass", "setHibernableEventClass", 0);

			//CustomTransport
	        digester.addObjectCreate("*/Transport/CustomTransport", SPEC_PCKG+".CustomTransportSpec");
	        digester.addSetNext("*/Transport/CustomTransport", "setInnerTransport", SPEC_PCKG+".TransportSpecInterface");
	        digester.addCallMethod("*/Transport/CustomTransport/TransportClass", "setTransportClass", 0);
	        digester.addObjectCreate("*/Transport/CustomTransport/Attribute", SPEC_PCKG+".AttributeSpec");
	        digester.addSetNext("*/Transport/CustomTransport/Attribute", "addAttribute", SPEC_PCKG+".AttributeSpec");
	        digester.addCallMethod("*/Transport/CustomTransport/Attribute/Name", "setName", 0);
	        digester.addCallMethod("*/Transport/CustomTransport/Attribute/Type", "setType", 0);
	        digester.addCallMethod("*/Transport/CustomTransport/Attribute/Value", "setValue", 0);

			//Log4JTransport
	        digester.addObjectCreate("*/Transport/Log4jTransport", SPEC_PCKG+".Log4jTransportSpec");
	        digester.addSetNext("*/Transport/Log4jTransport", "setInnerTransport", SPEC_PCKG+".TransportSpecInterface");
	        digester.addCallMethod("*/Transport/Log4jTransport/DebugLevel", "setDebugLevel", 0);
	        digester.addCallMethod("*/Transport/Log4jTransport/Prefix/EventClassName", "setEventClassNamePrefixType");
	        digester.addCallMethod("*/Transport/Log4jTransport/Prefix/TransportClassName", "setTransportClassNamePrefixType");
	        digester.addCallMethod("*/Transport/Log4jTransport/Prefix/Custom", "setCustomPrefixType");
	        digester.addCallMethod("*/Transport/Log4jTransport/Prefix/Custom", "setPrefix", 0);
			
			//EmailTransport
	        digester.addObjectCreate("*/Transport/MailTransport", SPEC_PCKG+".MailTransportSpec");
	        digester.addSetNext("*/Transport/MailTransport", "setInnerTransport", SPEC_PCKG+".TransportSpecInterface");
	        digester.addCallMethod("*/Transport/MailTransport/To/EventAttribute", "setToEventAttributeType");
	        digester.addCallMethod("*/Transport/MailTransport/To/EventAttribute", "setTo", 0);
	        digester.addCallMethod("*/Transport/MailTransport/To/Custom", "setToCustomType");
	        digester.addCallMethod("*/Transport/MailTransport/To/Custom", "setTo", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Cc/EventAttribute", "setCcEventAttributeType");
	        digester.addCallMethod("*/Transport/MailTransport/Cc/EventAttribute", "setCc", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Cc/Custom", "setCcCustomType");
	        digester.addCallMethod("*/Transport/MailTransport/Cc/Custom", "setCc", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Bcc/EventAttribute", "setBccEventAttributeType");
	        digester.addCallMethod("*/Transport/MailTransport/Bcc/EventAttribute", "setBcc", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Bcc/Custom", "setBccCustomType");
	        digester.addCallMethod("*/Transport/MailTransport/Bcc/Custom", "setBcc", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Subject/EventAttribute", "setSubjectEventAttributeType");
	        digester.addCallMethod("*/Transport/MailTransport/Subject/EventAttribute", "setSubject", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Subject/Custom", "setSubjectCustomType");
	        digester.addCallMethod("*/Transport/MailTransport/Subject/Custom", "setSubject", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Body/EventAttribute", "setBodyEventAttributeType");
	        digester.addCallMethod("*/Transport/MailTransport/Body/EventAttribute", "setBody", 0);
	        digester.addCallMethod("*/Transport/MailTransport/Body/Custom", "setBodyCustomType");
	        digester.addCallMethod("*/Transport/MailTransport/Body/Custom", "setBody", 0);
			digester.addCallMethod("*/Transport/MailTransport/SearchForAttachments", "setSearchForAttachments");
			
			//TeeTransport
			digester.addObjectCreate("*/Transport/TeeTransport", SPEC_PCKG+".TeeTransportSpec");
            digester.addSetNext("*/Transport/TeeTransport", "setInnerTransport", SPEC_PCKG+".TransportSpecInterface");
            digester.addCallMethod("*/Transport/TeeTransport/TeeJndiName", "setTeeJndiName", 0);
			
			
		    //XML2BeanTransformer
            digester.addObjectCreate("*/Transport/XML2BeanTransformer", SPEC_PCKG+".XML2BeanTransformerSpec");
            digester.addSetNext("*/Transport/XML2BeanTransformer", "setTransformer", SPEC_PCKG+".TransformerSpec");
			//Map2BeanTransformer
            digester.addObjectCreate("*/Transport/Map2BeanTransformer", SPEC_PCKG+".Map2BeanTransformerSpec");
            digester.addSetNext("*/Transport/Map2BeanTransformer", "setTransformer", SPEC_PCKG+".TransformerSpec");
			//Bean2MapTransformer
            digester.addObjectCreate("*/Transport/Bean2MapTransformer", SPEC_PCKG+".Bean2MapTransformerSpec");
            digester.addSetNext("*/Transport/Bean2MapTransformer", "setTransformer", SPEC_PCKG+".TransformerSpec");
			//Bean2XMLTransformer
            digester.addObjectCreate("*/Transport/Bean2XMLTransformer", SPEC_PCKG+".Bean2XMLTransformerSpec");
            digester.addSetNext("*/Transport/Bean2XMLTransformer", "setTransformer", SPEC_PCKG+".TransformerSpec");
            //CustomTransformer
            digester.addObjectCreate("*/Transport/CustomTransformer", SPEC_PCKG+".CustomTransformerSpec");
            digester.addSetNext("*/Transport/CustomTransformer", "setTransformer", SPEC_PCKG+".TransformerSpec");
            digester.addCallMethod("*/Transport/CustomTransformer/CustomTransformerClass", "setCustomTransformerClass", 0);
	        
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
            Logger.getLogger(this.getClass()).error("An error occurred while parsing specification file "+teeName);
			StringWriter sw = new StringWriter();
    		e.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
			throw e;
        }
        
	}
	
	
	
	/**
	 * Adds an event mapping specification to the events' map using the event class as key.
	 * 
	 * @param eventSpec
	 */
	public void addEventSpec(EventSpec eventSpec) {
		if (eventSpecMap.put(eventSpec.getEventClass(),eventSpec)!=null) {
			throw new RuntimeException("Duplicate event "+eventSpec.getEventClass());
		}
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
	
    public Map getEventSpecMap() {
        return eventSpecMap;
    }
	
    public UnknownEventSpec getUnknownEventSpec() {
        return unknownEventSpec;
    }
	
	
	public boolean areClassesLoadable() {
		ClassLoadVerifier clv = new ClassLoadVerifier();
		EventSpec eventSpec = null;
		Set keySet1 = eventSpecMap.keySet();
		boolean checkPassed = true;
		Iterator it = keySet1.iterator();
		while (it.hasNext() && checkPassed) {
			eventSpec = eventSpecMap.get(it.next());
			checkPassed = clv.verifyEventSpec(eventSpec);
		}
		if (checkPassed && unknownEventSpec!=null) {
			checkPassed = clv.verifyEventSpec(unknownEventSpec);
		}
		return checkPassed;
	}
	
	
	
}
