/*
 * Created on 8-giu-2005
 *
 */
package it.javalinux.tee.specification;



/**
 * @author Alessio
 *
 */
public class Log4jTransportSpec implements TransportSpec {
    
    public static final String EVENT_CLASS_NAME_PREFIX = "EventClassName";
    public static final String TRANSPORT_CLASS_NAME_PREFIX = "TransportClassName";
    public static final String CUSTOM_PREFIX = "Custom";
    
    private String debugLevel;
    private String prefixType;
    private String prefix;
    
    public String getDebugLevel() {
        return debugLevel;
    }
    public void setDebugLevel(String debugLevel) {
        this.debugLevel = debugLevel;
    }
    
    public void setEventClassNamePrefixType() {
        this.prefixType = EVENT_CLASS_NAME_PREFIX;
    }
    
    public void setTransportClassNamePrefixType() {
        this.prefixType = TRANSPORT_CLASS_NAME_PREFIX;
    }
    
    public void setCustomPrefixType() {
        this.prefixType = CUSTOM_PREFIX;
    }
    
    public String getPrefixType() {
        return prefixType;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String toString() {
        return new StringBuffer("Log4JTransport => debugLevel: %").append(debugLevel).append("% prefixType: %").append(prefixType)
        	.append("% prefix: %").append(prefix).append("%").toString();
    }
    
}
