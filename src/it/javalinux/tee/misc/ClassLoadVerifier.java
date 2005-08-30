package it.javalinux.tee.misc;

import it.javalinux.tee.specification.CustomTransformerSpec;
import it.javalinux.tee.specification.CustomTransportSpec;
import it.javalinux.tee.specification.EventSpec;
import it.javalinux.tee.specification.HandlerSpec;
import it.javalinux.tee.specification.TransformerSpec;
import it.javalinux.tee.specification.TransportSpec;
import it.javalinux.tee.specification.TransportSpecInterface;
import it.javalinux.tee.specification.UnknownEventSpec;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.jboss.logging.Logger;

/**
 * Verify whether classes referenced by event and
 * unknown event specification are loadable or not.
 * 
 * @author Alessio
 *
 */
public class ClassLoadVerifier {
	
	public boolean verifyEventSpec(EventSpec eventSpec) {
		try {
			this.verifyClass(eventSpec.getEventClass());
			List handlerSpecList = eventSpec.getHandlerSpecList();
			for (Iterator handlerIter = handlerSpecList.iterator(); handlerIter.hasNext(); ) {
				this.verifyHandlerSpec((HandlerSpec)handlerIter.next());
			}
			List transportSpecList = eventSpec.getTransportSpecList();
			for (Iterator transportIter = transportSpecList.iterator(); transportIter.hasNext(); ) {
				TransportSpec ts = (TransportSpec)transportIter.next();
				if (ts.getTransformer()!=null) {
					this.verifyTransformerSpec(ts.getTransformer());
				}
				if (ts.getInnerTransport()!=null) {
					this.verifyInnerTransportSpec(ts.getInnerTransport());
				}
			}
		} catch (ClassNotFoundException cnfe) {
			return false;
		}
		return true;
	}
	
	public boolean verifyEventSpec(UnknownEventSpec unknownEventSpec) {
		try {
			List handlerSpecList = unknownEventSpec.getHandlerSpecList();
			for (Iterator handlerIter = handlerSpecList.iterator(); handlerIter.hasNext(); ) {
				this.verifyHandlerSpec((HandlerSpec)handlerIter.next());
			}
			List transportSpecList = unknownEventSpec.getTransportSpecList();
			for (Iterator transportIter = transportSpecList.iterator(); transportIter.hasNext(); ) {
				TransportSpec ts = (TransportSpec)transportIter.next();
				if (ts.getTransformer()!=null) {
					this.verifyTransformerSpec(ts.getTransformer());
				}
				if (ts.getInnerTransport()!=null) {
					this.verifyInnerTransportSpec(ts.getInnerTransport());
				}
			}
		} catch (ClassNotFoundException cnfe) {
			return false;
		}
		return true;
	}
	
	private void verifyHandlerSpec(HandlerSpec handlerSpec) throws ClassNotFoundException {
		this.verifyClass(handlerSpec.getHandlerClass());
	}
	
	private void verifyTransformerSpec(TransformerSpec transformerSpec) throws ClassNotFoundException {
		if (transformerSpec instanceof CustomTransformerSpec) {
			CustomTransformerSpec cts = (CustomTransformerSpec)transformerSpec;
			this.verifyClass(cts.getCustomTransformerClass());
		}
	}
	
	private void verifyInnerTransportSpec(TransportSpecInterface transportSpec) throws ClassNotFoundException {
		if (transportSpec instanceof CustomTransportSpec) {
			CustomTransportSpec cts = (CustomTransportSpec)transportSpec;
			this.verifyClass(cts.getTransportClass());
		}
	}
	
	private void verifyClass(String cl) throws ClassNotFoundException {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(cl);
		} catch (ClassNotFoundException cnfe) {
			Logger.getLogger(this.getClass()).info("Class "+cl+" not found!");
			StringWriter sw = new StringWriter();
    		cnfe.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
			throw cnfe;
		}
	}
}
