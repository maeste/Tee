/*
 * Created on 10-giu-2005
 *
 */
package it.javalinux.tee.transport;

import it.javalinux.tee.event.Event;

import org.jboss.logging.Logger;

/**
 * @author Alessio
 *
 *	Log4jTransport is a simple transport that logs the events using Log4j.
 *
 */
public class Log4jTransport implements Transport {
    
    private String debugLevel;
    private String prefix;
    
    public void process(Event event) {
        if ("INFO".equalsIgnoreCase(debugLevel)) {
            Logger.getLogger(prefix).info(event); //Event should have a custom toString...
        } else if ("DEBUG".equalsIgnoreCase(debugLevel)) {
            Logger.getLogger(prefix).debug(event);
        } else if ("TRACE".equalsIgnoreCase(debugLevel)) {
            Logger.getLogger(prefix).trace(event);
        } else if ("ERROR".equalsIgnoreCase(debugLevel)) {
            Logger.getLogger(prefix).error(event);
        } else if ("FATAL".equalsIgnoreCase(debugLevel)) {
            Logger.getLogger(prefix).fatal(event);
        } else if ("WARN".equalsIgnoreCase(debugLevel)) {
            Logger.getLogger(prefix).warn(event);
        } else {
            Logger.getLogger(this.getClass()).info(new StringBuffer("Unsupported debug level \"").append(debugLevel)
                    .append("\"; choose between WARN, TRACE, DEBUG, INFO, ERROR and FATAL").toString());
        }
    }
    
    public String getDebugLevel() {
        return debugLevel;
    }
    public void setDebugLevel(String debugLevel) {
        this.debugLevel = debugLevel;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
