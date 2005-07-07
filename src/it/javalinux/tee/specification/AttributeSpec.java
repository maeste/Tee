/*
 * Created on 8-giu-2005
 *
 */
package it.javalinux.tee.specification;

/**
 * @author Alessio
 *
 */
public class AttributeSpec {
    
    private String name;
    private String type;
    private String value;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
