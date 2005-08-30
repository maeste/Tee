package it.javalinux.tee.transport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import org.jboss.logging.Logger;

import it.javalinux.tee.event.Event;

public class MailTransport implements Transport {
	
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String body;
	
	public void process(Event event) {
		try {
			List<String> toList = new ArrayList<String>();
			if (to!=null) {
				StringTokenizer st = new StringTokenizer(to," ,;");
				while (st.hasMoreTokens()) {
					toList.add(st.nextToken());
				}
			}
			List<String> ccList = new ArrayList<String>();
			if (cc!=null) {
				StringTokenizer stCc = new StringTokenizer(cc," ,;");
				while (stCc.hasMoreTokens()) {
					ccList.add(stCc.nextToken());
				}
			}
			List<String> bccList = new ArrayList<String>();
			if (bcc!=null) {
				StringTokenizer stBcc = new StringTokenizer(bcc," ,;");
				while (stBcc.hasMoreTokens()) {
					bccList.add(stBcc.nextToken());
				}
			}
			if (!toList.isEmpty() || !ccList.isEmpty() || !bccList.isEmpty()) {
				this.sendMail(toList,ccList,bccList,body,subject);
			} else {
				Logger.getLogger(this.getClass()).info("Email not sent since no recipient provided!");
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("An error occurred while transporting event");
            StringWriter sw = new StringWriter();
    		e.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
		}
	}
	
	
	/**
	 * 
	 * @param toRecipientList		
	 * @param ccRecipientList		
	 * @param bccRecipientList		
	 * @param message				
	 * @param subject				
	 * 
	 */
	private void sendMail(List toRecipientList, List ccRecipientList, List bccRecipientList,
										  String message, String subject) throws Exception {
		InitialContext ctx = new InitialContext();
		Session mailSession = (Session) ctx.lookup("java:/Mail");
		
		Address[] toAddresses = null;
		if (toRecipientList!=null) {
			List<Address> toList = new ArrayList<Address>();
			Iterator toIter = toRecipientList.iterator();
			while (toIter.hasNext()) {
				toList.add(new InternetAddress((String)toIter.next()));
			}
			toAddresses = (Address[])toList.toArray(new Address[toList.size()]);
		}
		Address[] ccAddresses = null;
		if (ccRecipientList!=null) {
			List<Address> ccList = new ArrayList<Address>();
			Iterator ccIter = ccRecipientList.iterator();
			while (ccIter.hasNext()) {
				ccList.add(new InternetAddress((String)ccIter.next()));
			}
			ccAddresses = (Address[])ccList.toArray(new Address[ccList.size()]);
		}
		Address[] bccAddresses = null;
		if (bccRecipientList!=null) {
			List<Address> bccList = new ArrayList<Address>();
			Iterator bccIter = bccRecipientList.iterator();
			while (bccIter.hasNext()) {
				bccList.add(new InternetAddress((String)bccIter.next()));
			}
			bccAddresses = (Address[])bccList.toArray(new Address[bccList.size()]);
		}
		MimeMessage msg = new MimeMessage(mailSession);
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setRecipients(Message.RecipientType.CC, ccAddresses);
		msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
		msg.setSubject(subject);
		Multipart multipart = new MimeMultipart();
		MimeBodyPart msgBody = new MimeBodyPart();
		msgBody.setText(message);
		multipart.addBodyPart(msgBody);
		msg.setContent(multipart);
		javax.mail.Transport.send(msg);
		StringBuffer sb = new StringBuffer("Email sent, TO: ");
		sb.append(toRecipientList);
		sb.append(" CC: ");
		sb.append(ccRecipientList);
		sb.append(" BCC: ");
		sb.append(bccRecipientList);
		Logger.getLogger(this.getClass()).debug(sb.toString());
	}


	public String getBcc() {
		return bcc;
	}
	


	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	


	public String getBody() {
		return body;
	}
	


	public void setBody(String body) {
		this.body = body;
	}
	


	public String getCc() {
		return cc;
	}
	


	public void setCc(String cc) {
		this.cc = cc;
	}
	


	public String getSubject() {
		return subject;
	}
	


	public void setSubject(String subject) {
		this.subject = subject;
	}
	


	public String getTo() {
		return to;
	}
	


	public void setTo(String to) {
		this.to = to;
	}
	
	
}
