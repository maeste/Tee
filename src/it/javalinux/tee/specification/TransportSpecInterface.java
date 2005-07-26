/*
 * Created on 7-giu-2005
 *
 */
package it.javalinux.tee.specification;


/**
 * @author Alessio
 *
 */
public interface TransportSpecInterface {
    
    public void setTransformer(TransformerSpec spec);
	public TransformerSpec getTransformer();
}
