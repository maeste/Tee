/*
 * Created on 7-giu-2005
 *
 */
package it.javalinux.tee.specification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jboss.logging.Logger;

/**
 * @author Alessio
 *
 */
public class UnknownEventSpec {
    
    private List handlerSpecList = new ArrayList();
    private List transportSpecList = new ArrayList();
    
    /**
     * 
     * @param handlerSpec
     */
    public void addHandler(HandlerSpec handlerSpec) {
        Logger.getLogger(this.getClass()).debug("Handler specification added");
        handlerSpecList.add(handlerSpec);
    }
    
    /**
     * 
     * @param transportSpec
     */
    public void addTransport(TransportSpec transportSpec) {
        Logger.getLogger(this.getClass()).debug("Transport specification added");
        transportSpecList.add(transportSpec);
    }
    
    /**
     * 
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("UnknowEvent => defined handlerSpecs: #");
        for (Iterator it = handlerSpecList.iterator(); it.hasNext(); ) {
            sb.append(((HandlerSpec)it.next()).getHandlerClass());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("#, defined transportSpecs: #");
        for (Iterator it = transportSpecList.iterator(); it.hasNext(); ) {
            TransportSpec ts =(TransportSpec)it.next(); 
            sb.append(ts.getClass().getName());
            sb.append("{");
            sb.append(ts.toString());
            sb.append("}");
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("#");
        return sb.toString();
    }

    public List getHandlerSpecList() {
        return handlerSpecList;
    }
    public List getTransportSpecList() {
        return transportSpecList;
    }
}
