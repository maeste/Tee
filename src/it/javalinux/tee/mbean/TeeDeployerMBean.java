/*
 * Created on 10-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import org.jboss.deployment.SubDeployerMBean;

public interface TeeDeployerMBean extends SubDeployerMBean {
//	default object name
	public static final javax.management.ObjectName OBJECT_NAME = org.jboss.mx.util.ObjectNameFactory.create("it.javalinux:Service=TeeDeployer");
	
	/**
	 * Returns true if this deployer can deploy the given DeploymentInfo.
	 * @return True if this deployer can deploy the given DeploymentInfo.
	 */
	boolean accepts(org.jboss.deployment.DeploymentInfo di) ;
	
	/**
	 * Describe <code>init</code> method here.
	 * @param di a <code>DeploymentInfo</code> value
	 * @throws DeploymentException if an error occurs
	 */
	void init(org.jboss.deployment.DeploymentInfo di) throws org.jboss.deployment.DeploymentException;
	
	/**
	 * Describe <code>create</code> method here.
	 * @param di a <code>DeploymentInfo</code> value
	 * @throws DeploymentException if an error occurs
	 */
	void create(org.jboss.deployment.DeploymentInfo di) throws org.jboss.deployment.DeploymentException;
	
	/**
	 * The <code>start</code> method starts all the mbeans in this DeploymentInfo..
	 * @param di a <code>DeploymentInfo</code> value
	 * @throws DeploymentException if an error occurs
	 */
	void start(org.jboss.deployment.DeploymentInfo di) throws org.jboss.deployment.DeploymentException;
	
	/**
	 * Undeploys the package at the url string specified. This will: Undeploy packages depending on this one. Stop, destroy, and unregister all the specified mbeans Unload this package and packages this package deployed via the classpath tag. Keep track of packages depending on this one that we undeployed so that they can be redeployed should this one be redeployed.
	 * @param di the <code>DeploymentInfo</code> value to stop.
	 */
	void stop(org.jboss.deployment.DeploymentInfo di) ;
	
	/**
	 * Describe <code>destroy</code> method here.
	 * @param di a <code>DeploymentInfo</code> value
	 */
	void destroy(org.jboss.deployment.DeploymentInfo di) ;
}
