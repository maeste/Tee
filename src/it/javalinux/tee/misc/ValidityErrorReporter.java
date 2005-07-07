/*
 * Created on 8-giu-2005
 *
 */
package it.javalinux.tee.misc;

import org.jboss.logging.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Alessio
 *
 */
public class ValidityErrorReporter implements ErrorHandler {
    
    public ValidityErrorReporter() {
    }
    
    /**
     * Prints a warning message
     * 
     * @param SAXParseException
     */
    public void warning(SAXParseException ex) throws SAXException {
        Logger.getLogger(this.getClass()).info("Parse warning!");
     }
    
    /**
     * Prints an error message and throw the exception specified as parameter
     * 
     * @param SAXParseException
     */
     public void error(SAXParseException ex) throws SAXException {
         Logger.getLogger(this.getClass()).info("Parse error!");
         throw ex; 
     }
     
     /**
      * Prints a fatal error message and throw the exception specified as parameter
      * 
      * @param SAXParseException
      */
     public void fatalError(SAXParseException ex) throws SAXException {
         Logger.getLogger(this.getClass()).info("Parse fatal error!");
         throw ex;
     }
}
