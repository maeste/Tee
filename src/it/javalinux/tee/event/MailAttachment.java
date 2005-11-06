package it.javalinux.tee.event;

/**
 * Base interface to be used for email attachment classes
 * that can be handled by Tee's MailTransport.
 * 
 * @author Alessio
 *
 */
public interface MailAttachment {
	
	public byte[] getData();
	public String getMimeType();
	public String getFilename();
	
}
