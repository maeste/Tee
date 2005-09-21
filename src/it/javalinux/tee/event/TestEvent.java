/*
 * Created on 21-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.event;

import it.javalinux.tee.annotations.TeeEvent;
import it.javalinux.tee.annotations.TeeEventInterceptor;

@TeeEvent (TeeName="gino") 
public class TestEvent implements Event {
	private String fooString;
	private Integer fooInteger;
	private int fooInt;
	private Float fooFloat;
	
	/**
	 * @param fooString
	 * @param fooInteger
	 * @param fooInt
	 * @param fooFloat
	 */
	@TeeEventInterceptor
	public TestEvent(String fooString, Integer fooInteger, int fooInt, Float fooFloat) {
		super();
		// TODO Auto-generated constructor stub
		this.fooString = fooString;
		this.fooInteger = fooInteger;
		this.fooInt = fooInt;
		this.fooFloat = fooFloat;
	}
	
	public TestEvent() {
		
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
	

	public String toString() {
	    return "fooString: "+fooString+" fooInteger: "+fooInteger+" fooFloat: "+fooFloat+" fooint: "+fooInt;
    }


	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.toString().equals(obj.toString());
	}


	public int hashCode() {
		// TODO Auto-generated method stub
		return this.toString().hashCode();
	}
	
	
	


}
