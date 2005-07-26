/*
 * Created on 22-lug-2005
 *
 */
package it.javalinux.tee.specification;

import org.jboss.logging.Logger;

/**
 * @author Alessio
 *
 */
public class TeeTransportSpec implements TransportSpecInterface {
    
    private String teeJndiName;
	private TransformerSpec transformerSpec;
    
    public TeeTransportSpec() {
    }
    
    public void setTeeJndiName(String teeJndiName) {
        this.teeJndiName = teeJndiName;
    }
    
    public String getTeeJndiName() {
        return teeJndiName;
    }
    
    public String toString() {
        return "TeeTransport";
    }

	public TransformerSpec getTransformer() {
		return transformerSpec;
	}
	

	public void setTransformer(TransformerSpec transformerSpec) {
		Logger.getLogger(this.getClass()).info("Transormer setted:"+ transformerSpec.toString());
		this.transformerSpec = transformerSpec;
	}
}
