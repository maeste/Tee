/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.misc;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.handler.Handler;
import it.javalinux.tee.specification.AttributeSpec;
import it.javalinux.tee.specification.CustomTransformerSpec;
import it.javalinux.tee.specification.CustomTransportSpec;
import it.javalinux.tee.specification.HandlerSpec;
import it.javalinux.tee.specification.HibernateTransportSpec;
import it.javalinux.tee.specification.Log4jTransportSpec;
import it.javalinux.tee.specification.TeeTransportSpec;
import it.javalinux.tee.specification.TransformerSpec;
import it.javalinux.tee.specification.TransportSpec;
import it.javalinux.tee.specification.XML2BeanTransformerSpec;
import it.javalinux.tee.transport.HibernateTransport;
import it.javalinux.tee.transport.Log4jTransport;
import it.javalinux.tee.transport.Transport;
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
            handler.logMyCall();
            handler.process(event, handlerSpec.getLogMe());
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
    public void processWithTransport(Event event, TransportSpec transportSpec) {
        try {
            if (transportSpec instanceof Log4jTransportSpec) {
                Logger.getLogger(this.getClass()).debug("Passing event to a Log4jTransport");
                Log4jTransportSpec spec = (Log4jTransportSpec)transportSpec;
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
            } else if (transportSpec instanceof HibernateTransportSpec) {
                Logger.getLogger(this.getClass()).debug("Passing event to an HibernateTransport");
                HibernateTransportSpec spec = (HibernateTransportSpec)transportSpec;
                HibernateTransport transport = new HibernateTransport();
                transport.setHibernableEventClass(spec.getHibernableEventClass());
                transport.process(event);
            } else if (transportSpec instanceof CustomTransportSpec) {
                CustomTransportSpec spec = (CustomTransportSpec)transportSpec;
                Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to a CustomTransport: class ")
                        .append(spec.getTransportClass()).toString());
                Transport transport = (Transport)(Class.forName(spec.getTransportClass()).newInstance());
                for (Iterator it = spec.getAttributeSpecList().iterator(); it.hasNext(); ) {
                    AttributeSpec attributeSpec = (AttributeSpec)it.next();
                    BeanUtils.copyProperty(transport, attributeSpec.getName(), attributeSpec.getValue());
                }
                transport.process(event);
            } else if (transportSpec instanceof TeeTransportSpec) {
                TeeTransportSpec spec = (TeeTransportSpec)transportSpec;
                Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to a TeeTransport: jndiName ")
                        .append(spec.getTeeJndiName()).toString());
                Object[] parArray = {event};
                String[] signArray = {"it.javalinux.tee.event.Event"};
                ServiceLocator.getInstance().callMBean(spec.getTeeJndiName(),"process",parArray, signArray);
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("An error occurred during event processing by transport " + transportSpec.getClass());
            StringWriter sw = new StringWriter();
    		e.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
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
        if (transformerSpec instanceof XML2BeanTransformerSpec) {
            Logger.getLogger(this.getClass()).debug("Passing event to a XML2BeanTransformer");
            XML2BeanTransformer transformer = new XML2BeanTransformer();
            result = transformer.transform(event);
        } else if (transformerSpec instanceof CustomTransformerSpec) {
            Logger.getLogger(this.getClass()).debug("Passing event to a CustomTransformer");
            CustomTransformerSpec spec = (CustomTransformerSpec)transformerSpec;
            
            Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to a CustomTransformer: class ")
                    .append(spec.getCustomTransformerClass()).toString());
            TransformerInterface transformer = (TransformerInterface)(Class.forName(spec.getCustomTransformerClass()).newInstance());
            result = transformer.transform(event);
        } else {
            throw new IllegalArgumentException("unknown transformerSpec: "+transformerSpec);
        }
        if (result!=null) {
            Logger.getLogger(this.getClass()).info("Event transformed into a "+result.getClass()+" event.");
        }
        return result;
    }
    
}
