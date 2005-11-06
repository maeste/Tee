/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.misc;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.event.MailAttachment;
import it.javalinux.tee.exception.TransformationException;
import it.javalinux.tee.handler.Handler;
import it.javalinux.tee.specification.AttributeSpec;
import it.javalinux.tee.specification.Bean2MapTransformerSpec;
import it.javalinux.tee.specification.Bean2XMLTransformerSpec;
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
import it.javalinux.tee.transport.tranformer.Bean2MapTransformer;
import it.javalinux.tee.transport.tranformer.Bean2XMLTransformer;
import it.javalinux.tee.transport.tranformer.Map2BeanTransformer;
import it.javalinux.tee.transport.tranformer.TransformerInterface;
import it.javalinux.tee.transport.tranformer.XML2BeanTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public void processWithHandler(Event event, HandlerSpec handlerSpec) throws Exception {
        Logger.getLogger(this.getClass()).debug(new StringBuffer("Passing event to handler class ")
                .append(handlerSpec.getHandlerClass()).toString());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Logger.getLogger(this.getClass()).debug("classLoader: "+classLoader);
        Handler handler = (Handler)(classLoader.loadClass(handlerSpec.getHandlerClass()).newInstance());
        handler.process(event);
    }
    
    /**
     * Obtains a new instance of the Transport specified by transportSpec, populates it with the
     * required parameters according to its type, then passes it the event to be processed.
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
			Map propertyMap = BeanUtils.describe(event);
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getToType())) {
				transport.setTo((String)propertyMap.get(spec.getTo()));
			} else { //Custom or not set
				transport.setTo(spec.getTo());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getCcType())) {
				transport.setCc((String)propertyMap.get(spec.getCc()));
			} else { //Custom
				transport.setCc(spec.getCc());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getBccType())) {
				transport.setBcc((String)propertyMap.get(spec.getBcc()));
			} else { //Custom
				transport.setBcc(spec.getBcc());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getSubjectType())) {
				transport.setSubject((String)propertyMap.get(spec.getSubject()));
			} else { //Custom
				transport.setSubject(spec.getSubject());
			}
			if (MailTransportSpec.EVENT_ATTRIBUTE.equalsIgnoreCase(spec.getBodyType())) {
				transport.setBody((String)propertyMap.get(spec.getBody()));
			} else { //Custom
				transport.setBody(spec.getBody());
			}
			if (spec.isSearchForAttachments()) {
				transport.setAttachments(this.getAttachments(event.getClass(),event));
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
            ServiceLocator.getInstance().callMBean("it.javalinux:service="+spec.getTeeJndiName(),"singleThreadProcess",parArray, signArray);
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
		} else if (transformerSpec instanceof Bean2MapTransformerSpec) {
			Logger.getLogger(this.getClass()).debug("Passing event to a Bean2MapTransformer");
            transformer = new Bean2MapTransformer();
            result = transformer.transform(event);
		} else if (transformerSpec instanceof Bean2XMLTransformerSpec) {
			Logger.getLogger(this.getClass()).debug("Passing event to a Bean2XMLTransformer");
            transformer = new Bean2XMLTransformer();
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
	
	
	/**
	 * This method scans the target object and looks for fields being 
	 * instance of MailAttachment or a collection of it.
	 * Returns a list containing those MailAttachment fields.
	 * 
	 * @param cl
	 * @param targetObj
	 * @return
	 * @throws Exception
	 */
	private List<MailAttachment> getAttachments(Class cl, Object targetObj) throws Exception {
		List<MailAttachment> attList = new ArrayList<MailAttachment>();
		Field[] fields = cl.getDeclaredFields(); //extract public fields only...
		Field f = null;
		int i=0;
		while (i<fields.length) {
			boolean wasAccessible = fields[i].isAccessible();
			fields[i].setAccessible(true);
			Object obj = fields[i].get(targetObj);
			if (obj!=null) {
				if (obj instanceof MailAttachment) {
					attList.add((MailAttachment)obj);
				} else if (obj instanceof Collection) {
					for (Iterator it=((Collection)obj).iterator(); it.hasNext(); ) {
						Object element = it.next();
						if (element instanceof MailAttachment) {
							attList.add((MailAttachment)element);
						}
					}
				}
			}
			if (!wasAccessible) {
				fields[i].setAccessible(false);
			}
			i++;
		}
		if (cl.getSuperclass()!=null) {
			attList.addAll(this.getAttachments(cl.getSuperclass(),targetObj)); //let's check the superclass...
		}
		return attList;
	}
	
}
