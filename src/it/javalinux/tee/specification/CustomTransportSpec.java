/*
 * Created on 8-giu-2005
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
public class CustomTransportSpec implements TransportSpec {
    
    private String transportClass;
    private List attributeSpecList = new ArrayList();
    
    /**
     * 
     * @param transportSpec
     */
    public void addAttribute(AttributeSpec attributeSpec) {
        Logger.getLogger(this.getClass()).debug("Attribute specification added");
        attributeSpecList.add(attributeSpec);
    }
    
    public List getAttributeSpecList() {
        return attributeSpecList;
    }
    public String getTransportClass() {
        return transportClass;
    }
    public void setTransportClass(String transportClass) {
        this.transportClass = transportClass;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("CustomTransport ");
        sb.append(transportClass);
        sb.append(" => attributes: %");
        for (Iterator it = attributeSpecList.iterator(); it.hasNext(); ) {
            AttributeSpec as = (AttributeSpec)it.next();
            sb.append("(");
            sb.append(as.getType());
            sb.append(" ");
            sb.append(as.getName());
            sb.append(" ");
            sb.append(as.getValue());
            sb.append(")");
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("%");
        return sb.toString();
    }
    
}
