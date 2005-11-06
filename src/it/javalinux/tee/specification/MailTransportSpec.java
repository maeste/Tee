package it.javalinux.tee.specification;


public class MailTransportSpec implements TransportSpecInterface {
	
	public static final String EVENT_ATTRIBUTE = "EventAttribute";
    public static final String CUSTOM = "Custom";
    
    protected String to;
	private String toType;
	private String cc;
	private String ccType;
	private String bcc;
	private String bccType;
	private String subject;
	private String subjectType;
	private String body;
	private String bodyType;
	private boolean searchForAttachments = false;
	private TransformerSpec transformerSpec;
	
	
	public String getBcc() {
		return bcc;
	}
	
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
	public String getBccType() {
		return bccType;
	}
	
	public void setBccType(String bccType) {
		this.bccType = bccType;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getBodyType() {
		return bodyType;
	}
	
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}
	
	public String getCc() {
		return cc;
	}
	
	public void setCc(String cc) {
		this.cc = cc;
	}
	
	public String getCcType() {
		return ccType;
	}
	
	public void setCcType(String ccType) {
		this.ccType = ccType;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getSubjectType() {
		return subjectType;
	}
	
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getToType() {
		return toType;
	}
	
	public void setToType(String toType) {
		this.toType = toType;
	}
	
	public String toString() {
        StringBuffer sb = new StringBuffer();
		sb.append("EmailTransport => to: %").append(to).append("% toType: %").append(toType);
		sb.append("% cc: %").append(cc).append("% ccType: %").append(ccType);
		sb.append("% bcc: %").append(bcc).append("% bccType: %").append(bccType);
		sb.append("% subject: %").append(subject).append("% subjectType: %").append(subjectType);
		sb.append("% body: %").append(body).append("% bodyType: %").append(bodyType);
		sb.append("% attachments: %").append(searchForAttachments);
		sb.append("%");
		return sb.toString();
    }

	public TransformerSpec getTransformer() {
		return transformerSpec;
	}

	public void setTransformer(TransformerSpec transformerSpec) {
		this.transformerSpec = transformerSpec;
	}
	
	public void setToEventAttributeType() {
		this.toType = EVENT_ATTRIBUTE;
	}
	
	public void setToCustomType() {
		this.toType = CUSTOM;
	}
	
	public void setCcEventAttributeType() {
		this.ccType = EVENT_ATTRIBUTE;
	}
	
	public void setCcCustomType() {
		this.ccType = CUSTOM;
	}
	
	public void setBccEventAttributeType() {
		this.bccType = EVENT_ATTRIBUTE;
	}
	
	public void setBccCustomType() {
		this.bccType = CUSTOM;
	}
	
	public void setSubjectEventAttributeType() {
		this.subjectType = EVENT_ATTRIBUTE;
	}
	
	public void setSubjectCustomType() {
		this.subjectType = CUSTOM;
	}
	
	public void setBodyEventAttributeType() {
		this.bodyType = EVENT_ATTRIBUTE;
	}
	
	public void setBodyCustomType() {
		this.bodyType = CUSTOM;
	}
	
	public void setSearchForAttachments() {
		this.searchForAttachments = true;
	}
	
	public boolean isSearchForAttachments() {
		return this.searchForAttachments;
	}
}
