package it.javalinux.tee.event;



public final class NullEvent implements Event {
	
	private String message;
	private Long interceptionTimeMillis;
	
	public NullEvent() {
		
	}
	
	public NullEvent(String message) {
		this.message = message;
	}

	public Long getInterceptionTimeMillis() {
		return this.interceptionTimeMillis;
	}
	
	public void setInterceptionTimeMillis(Long interceptionTimeMillis) {
		this.interceptionTimeMillis = interceptionTimeMillis;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		return super.toString()+" InterceptionTimeMillis: "+interceptionTimeMillis+" Message: "+message;
	}
	
	
}
