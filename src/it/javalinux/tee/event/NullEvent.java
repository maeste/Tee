package it.javalinux.tee.event;


public final class NullEvent implements Event {
	
	private String message;
	
	public NullEvent() {
		
	}
	
	public NullEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		return super.toString()+" Message: "+message;
	}
	
	
}
