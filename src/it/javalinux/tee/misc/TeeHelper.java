/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.misc;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.exception.TransformationException;
import it.javalinux.tee.handler.Handler;
import it.javalinux.tee.specification.AttributeSpec;
import it.javalinux.tee.specification.CustomTransformerSpec;
import it.javalinux.tee.specification.CustomTransportSpec;
import it.javalinux.tee.specification.HandlerSpec;
import it.javalinux.tee.specification.HibernateTransportSpec;
import it.javalinux.tee.specification.Log4jTransportSpec;
import it.javalinux.tee.specification.MailTransportSpec;
import it.javalinux.tee.specification.Map2BeanTransformerSpec;
import it.javalinux.tee.specification.TeeTransportSpec;
import it.javalinux.tee.specification.TransformerSpec;
import it.javalinux.tee.specification.TransportSpec;
import it.javalinux.tee.specification.TransportSpecInterface;
import it.javalinux.tee.specification.XML2BeanTransformerSpec;
import it.javalinux.tee.transport.HibernateTransport;
import it.javalinux.tee.transport.Log4jTransport;
import it.javalinux.tee.transport.MailTransport;
import it.javalinux.tee.transport.Transport;
import it.javalinux.tee.transport.tranformer.Map2BeanTransformer;
import it.javalinux.tee.transport.tranformer.TransformerInterface;
import it.javalinux.tee.transport.tranformer.XML2BeanTransformer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.logging.Logger;

/**
 * @author Alessio
 * 
 */
public class TeeHelper {
    
    /**
     * Obtains a new instance of the Handler specified by handlerSpec,
     * then passes it the event to be processed.
     * 
     * 
     * @param event
     * @param handlerSpec
     */
    public void processWithHandler(Event event, HandlerSpec handlerSpec) {
        try {
            Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to handler class ")
                    .append(handlerSpec.getHandlerClass()).toString());
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Logger.getLogger(this.getClass()).info("classLoader: "+classLoader);
            Handler handler = (Handler)(classLoader.loadClass(handlerSpec.getHandlerClass()).newInstance());
            handler.process(event);
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("An error occurred during event processing by handler "+
                    handlerSpec.getHandlerClass());
            StringWriter sw = new StringWriter();
    		e.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
        }
    }
    
    /**
     * Obtains a new instance of the Transport specified by transportSpec, populates it with the required parameters
     * according to its type, then passes it the event to be processed.
     *  
     * @param event
     * @param transportSpec
     */
    public void processWithTransport(Event event, TransportSpec transportSpec) throws Exception {
  
		Logger.getLogger(this.getClass()).info("Transport = " + transportSpec);
		if (transportSpec.getTransformer()!=null) {
		    Logger.getLogger(this.getClass()).info("Transformer = " + transportSpec.getTransformer().getClass().getCanonicalName() );
            event = this.transformEvent(event, transportSpec.getTransformer());
		}
		TransportSpecInterface specializedTransport = transportSpec.getInnerTransport();
        if (specializedTransport instanceof Log4jTransportSpec) {
            Logger.getLogger(this.getClass()).debug("Passing event to a Log4jTransport");
            Log4jTransportSpec spec = (Log4jTransportSpec)specializedTransport;
            Log4jTransport transport = new Log4jTransport();
            transport.setDebugLevel(spec.getDebugLevel());
            if (Log4jTransportSpec.EVENT_CLASS_NAME_PREFIX.equalsIgnoreCase(spec.getPrefixType())) {
                transport.setPrefix(event.getClass().getName());
            } else if (Log4jTransportSpec.TRANSPORT_CLASS_NAME_PREFIX.equalsIgnoreCase(spec.getPrefixType())) {
                transport.setPrefix(transport.getClass().getName());
            } else {
                transport.setPrefix(spec.getPrefix());
            }
            transport.process(event);
		} else if (specializedTransport instanceof MailTransportSpec) {
            Logger.getLogger(this.getClass()).debug("Passing event to a MailTransport");
            MailTransportSpec spec = (MailTransportSpec)specializedTransport;
            MailTransport transport = new MailTransport();
			ReflectionHelper refHelper = new ReflectionHelper();
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getToType())) {
				transport.setTo(refHelper.getStringAttribute(event,spec.getTo()));
			} else { //Custom
				transport.setTo(spec.getTo());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getCcType())) {
				transport.setCc(refHelper.getStringAttribute(event,spec.getCc()));
			} else { //Custom
				transport.setCc(spec.getCc());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getBccType())) {
				transport.setBcc(refHelper.getStringAttribute(event,spec.getBcc()));
			} else { //Custom
				transport.setBcc(spec.getBcc());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getSubjectType())) {
				transport.setSubject(refHelper.getStringAttribute(event,spec.getSubject()));
			} else { //Custom
				transport.setSubject(spec.getSubject());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getBodyType())) {
				transport.setBody(refHelper.getStringAttribute(event,spec.getBody()));
			} else { //Custom
				transport.setBody(spec.getBody());
			}
            transport.process(event);
        } else if (specializedTransport instanceof HibernateTransportSpec) {
            Logger.getLogger(this.getClass()).debug("Passing event to an HibernateTransport");
            HibernateTransportSpec spec = (HibernateTransportSpec)specializedTransport;
            HibernateTransport transport = new HibernateTransport();
            transport.setHibernableEventClass(spec.getHibernableEventClass());
            transport.process(event);
        } else if (specializedTransport instanceof CustomTransportSpec) {
            CustomTransportSpec spec = (CustomTransportSpec)specializedTransport;
            Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to a CustomTransport: class ")
                    .append(spec.getTransportClass()).toString());
            Transport transport = (Transport)(Class.forName(spec.getTransportClass()).newInstance());
            for (Iterator it = spec.getAttributeSpecList().iterator(); it.hasNext(); ) {
                AttributeSpec attributeSpec = (AttributeSpec)it.next();
                BeanUtils.copyProperty(transport, attributeSpec.getName(), attributeSpec.getValue());
            }
            transport.process(event);
        } else if (specializedTransport instanceof TeeTransportSpec) {
            TeeTransportSpec spec = (TeeTransportSpec)specializedTransport;
            Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to a TeeTransport: jndiName ")
                    .append(spec.getTeeJndiName()).toString());
            Object[] parArray = {event};
            String[] signArray = {"it.javalinux.tee.event.Event"};
            ServiceLocator.getInstance().callMBean("it.javalinux:service="+spec.getTeeJndiName(),"process",parArray, signArray);
        }
        
    }
    
    
    /**
     * 
     * @param event
     * @param transformerSpec
     * @return Event
     */
    public Event transformEvent(Event event, TransformerSpec transformerSpec) throws Exception {
        Event result = null;
		TransformerInterface transformer;
        if (transformerSpec instanceof XML2BeanTransformerSpec) {
            Logger.getLogger(this.getClass()).debug("Passing event to a XML2BeanTransformer");
            transformer = new XML2BeanTransformer();
            result = transformer.transform(event);
        } else if (transformerSpec instanceof CustomTransformerSpec) {
            Logger.getLogger(this.getClass()).debug("Passing event to a CustomTransformer");
            CustomTransformerSpec spec = (CustomTransformerSpec)transformerSpec;
            
            Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to a CustomTransformer: class ")
                    .append(spec.getCustomTransformerClass()).toString());
            transformer = (TransformerInterface)(Class.forName(spec.getCustomTransformerClass()).newInstance());
            result = transformer.transform(event);
		} else if (transformerSpec instanceof Map2BeanTransformerSpec) {
			Logger.getLogger(this.getClass()).debug("Passing event to a Map2BeanTransformer");
            transformer = new Map2BeanTransformer();
            result = transformer.transform(event);
        } else {
            throw new IllegalArgumentException("unknown transformerSpec: "+transformerSpec);
        }
        if (result!=null) {
            Logger.getLogger(this.getClass()).info("Event transformed into a "+result.getClass()+" event.");
        } else {
			throw new TransformationException("Transformer" + transformer.getClass().getCanonicalName() + "return an ivalid (null) Event");
        }
        return result;
    }
	
}
