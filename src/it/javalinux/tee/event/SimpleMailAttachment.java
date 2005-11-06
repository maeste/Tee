package it.javalinux.tee.event;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jboss.logging.Logger;

/**
 * This class provides a simple implementation of MailAttachment,
 * to be used for common attachments.
 * 
 * @author Alessio
 *
 */
public class SimpleMailAttachment implements MailAttachment {
	
	private String mimeType;
	private String filename;
	private byte[] data;
	
	/**
	 * Standard constructor, simply sets the prodived parameters.
	 * 
	 * @param mimeType
	 * @param filename
	 * @param data
	 */
	public SimpleMailAttachment(String mimeType, String filename, byte[] data) {
		this.filename = filename;
		this.mimeType = mimeType;
		this.data = data;
	}
	
	
	/**
	 * Creates a new SimpleMailAttachment converting the provided data to
	 * byte[] through a ObjectOutputStream.
	 * 
	 * @param mimeType
	 * @param filename
	 * @param data
	 */
	public SimpleMailAttachment(String mimeType, String filename, Object data) {
		this.filename = filename;
		this.mimeType = mimeType;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(data);
			oos.flush();
			oos.close();
			this.data = bos.toByteArray();
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("Cannot convert provided data to byte[].");
			StringWriter sw = new StringWriter();
    		e.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
		}
	}
	
	/**
	 * Creates a new SimpleMailAttachment object containing a plain text file.
	 * 
	 * @param simpleText
	 * @param filename
	 */
	public SimpleMailAttachment(String simpleText, String filename) {
		this.mimeType = "text/plain";
		this.filename = filename;
		this.data = simpleText.getBytes();
	}
	
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
}
