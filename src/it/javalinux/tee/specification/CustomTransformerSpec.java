/*
 * Created on 22-lug-2005
 *
 */
package it.javalinux.tee.specification;

/**
 * @author Alessio
 *
 */
public class CustomTransformerSpec implements TransformerSpec {
    
    private String customTransformerClass;
    
    public CustomTransformerSpec() {
        
    }
    
    public String getCustomTransformerClass() {
        return customTransformerClass;
    }
    public void setCustomTransformerClass(String customTransformerClass) {
        this.customTransformerClass = customTransformerClass;
    }
    
    public String toString() {
        return new StringBuffer("CustomTransformer => customTransformerClass: %").append(customTransformerClass).append("%").toString();
    }
}
