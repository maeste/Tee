/*
 * Created on 7-giu-2005
 *
 */
package it.javalinux.tee.specification;

/**
 * @author Alessio
 *
 */
public class TransportSpec {
    TransportSpecInterface innerTransport;

	public TransportSpecInterface getInnerTransport() {
		return innerTransport;
	}
	

	public void setInnerTransport(TransportSpecInterface innerTransport) {
		this.innerTransport = innerTransport;
	}


	public TransformerSpec getTransformer() {
		// TODO Auto-generated method stub
		return innerTransport.getTransformer();
	}


	public void setTransformer(TransformerSpec spec) {
		// TODO Auto-generated method stub
		innerTransport.setTransformer(spec);
	}
	
	
    
	
	
}
