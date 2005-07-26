/*
 * Created on 7-giu-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import it.javalinux.tee.event.Event;
import it.javalinux.tee.misc.SpecificationDigester;
import it.javalinux.tee.misc.TeeHelper;
import it.javalinux.tee.specification.EventSpec;
import it.javalinux.tee.specification.HandlerSpec;
import it.javalinux.tee.specification.TransportSpec;
import it.javalinux.tee.specification.UnknownEventSpec;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.aspects.Injected;
import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author oracle
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Tee extends ServiceMBeanSupport implements TeeMBean  {
    

    
	private String teeName;
    
    private SpecificationDigester specDigester = null;
	
	@Injected TransactionManager tm;
	
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
		specDigester = new SpecificationDigester();
        specDigester.readSpecification(teeName);
	}
	
	
	/* (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#stopService()
	 */
	protected void stopService() throws Exception {
		super.stopService();
		Logger.getLogger(this.getClass()).debug("stopService");
	}

	
	

    /**
     * @param event
     * 
     */
	//@Asynchronous
	public void process(Event event) {



		Transaction motherTransaction = null;
		
		try {
			
			if (tm.getStatus() != Status.STATUS_NO_TRANSACTION) {
				Logger.getLogger(this.getClass()).debug("suspend old transaction");
				motherTransaction = tm.suspend();
			}
			Logger.getLogger(this.getClass()).debug("Starting transaction");
			tm.begin();
			
		    Logger.getLogger(this.getClass()).info("Processing event of class "+event.getClass().getName());
			//get handler and/or transport right for this event 
			// and invoke them passing event and log option
		    TeeHelper helper = new TeeHelper();
		    Object obj = specDigester.getEventSpecMap().get(event.getClass().getName());
	        if (obj!=null) {
	            EventSpec eventSpec = (EventSpec)obj;
//                if (eventSpec.getTransformer()!=null) {
//                    event = helper.transformEvent(event, eventSpec.getTransformer());
//                }
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
                UnknownEventSpec unknownEventSpec = specDigester.getUnknownEventSpec();
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
				tm.setRollbackOnly();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	
			try {
				if (tm.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
					Logger.getLogger(this.getClass()).debug("rollback transaction");
					tm.rollback();
				} else {
					Logger.getLogger(this.getClass()).debug("commit transaction");
					tm.commit();
				}
				
				if (motherTransaction != null ) {
					Logger.getLogger(this.getClass()).debug("resume old transaction");
					tm.resume(motherTransaction);
				}
				
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
	
	@Injected 
	void setTransactionManager(TransactionManager tm) {
		this.tm = tm;
	}
	
}
