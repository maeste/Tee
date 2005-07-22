/*
 * Created on 22-lug-2005
 *
 */
package it.javalinux.tee.specification;

/**
 * @author Alessio
 *
 */
public class TeeTransportSpec implements TransportSpec {
    
    private String teeJndiName;
    
    public TeeTransportSpec() {
    }
    
    public void setTeeJndiName(String teeJndiName) {
        this.teeJndiName = teeJndiName;
    }
    
    public String getTeeJndiName() {
        return teeJndiName;
    }
    
    public String toString() {
        return "TeeTransport";
    }
}
