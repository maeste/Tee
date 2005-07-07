/*
 * Created on 7-giu-2005
 *
 */
package it.javalinux.tee.specification;

/**
 * @author Alessio
 *
 */
public class HandlerSpec {
    
    private String handlerClass;
    private boolean logMe;
    
    public HandlerSpec() {
    }
    
    public String getHandlerClass() {
        return handlerClass;
    }
    
    public void setHandlerClass(String handlerClass) {
        this.handlerClass = handlerClass;
    }
    
    public boolean getLogMe() {
        return logMe;
    }
    
    public void setLogMe(boolean logMe) {
        this.logMe = logMe;
    }
    
}
