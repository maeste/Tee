/*
 * Created on 8-giu-2005
 *
 */
package it.javalinux.tee.specification;

/**
 * @author Alessio
 *
 */
public class HibernateTransportSpec implements TransportSpecInterface {
    
    private String hibernableEventClass;
	private TransformerSpec transformerSpec;
    
    
    public String getHibernableEventClass() {
        return hibernableEventClass;
    }
    public void setHibernableEventClass(String hibernableEventClass) {
        this.hibernableEventClass = hibernableEventClass;
    }
    
    public String toString() {
        return new StringBuffer("HibernateTransport => hibernableEventClass: %").append(hibernableEventClass).append("%").toString();
    }

	public TransformerSpec getTransformer() {
		return transformerSpec;
	}
	

	public void setTransformer(TransformerSpec transformerSpec) {
		this.transformerSpec = transformerSpec;
	}
}
