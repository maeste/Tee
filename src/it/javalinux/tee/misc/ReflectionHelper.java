package it.javalinux.tee.misc;

import java.lang.reflect.Field;

import org.jboss.logging.Logger;

public class ReflectionHelper {
	
	public String getStringAttribute(Object obj, String attributeName) throws Exception {
		try {
			System.out.println(obj.getClass());
			Field field = this.getField(obj.getClass(),attributeName);
			return (String)field.get(obj);
		} catch (ClassCastException cce) {
			Logger.getLogger(this.getClass()).error("Attribute "+attributeName+" must be a String!");
			throw cce;
		} catch (NullPointerException npe) {
			Logger.getLogger(this.getClass()).error("Attribute "+attributeName+" not found!");
			throw npe;
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("Error getting value of attribute "+attributeName);
			throw e;
		}
	}
	
	public Field getField(Class cl, String fieldName) throws Exception {
		if (cl==null) {
			return null;
		} else {
			Field[] fields = cl.getDeclaredFields(); //extract public fields only...
			Field f = null;
			int i=0;
			boolean found = false;
			while (i<fields.length && !found) {
				//Logger.getLogger(this.getClass()).info(fields[i]);
				if (fields[i].getName().equalsIgnoreCase(fieldName)) {
					f = fields[i];
					found = true;
				}
				i++;
			}
			if (f==null) {
				return this.getField(cl.getSuperclass(),fieldName); //let's check the superclass...
			} else {
				f.setAccessible(true);
				return f;
			}
		}
	}
	
}
