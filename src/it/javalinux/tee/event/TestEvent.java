/*
 * Created on 21-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.event;

import java.util.Date;

public class TestEvent implements Event {
	private String fooString;
	private Integer fooInteger;
	private int fooInt;
	private Float fooFloat;
	
	public Date getInterceptTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInterceptorName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Float getFooFloat() {
		return fooFloat;
	}
	

	public void setFooFloat(Float fooFloat) {
		this.fooFloat = fooFloat;
	}
	

	public int getFooInt() {
		return fooInt;
	}
	

	public void setFooInt(int fooInt) {
		this.fooInt = fooInt;
	}
	

	public Integer getFooInteger() {
		return fooInteger;
	}
	

	public void setFooInteger(Integer fooInteger) {
		this.fooInteger = fooInteger;
	}
	

	public String getFooString() {
		return fooString;
	}
	

	public void setFooString(String fooString) {
		this.fooString = fooString;
	}
	

	
	


}