/*
 * Created on 10-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.ObjectName;

import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.deployment.SubDeployer;
import org.jboss.deployment.SubDeployerSupport;
import org.jboss.util.file.ArchiveBrowser;
import org.jboss.util.file.ClassFileFilter;

public class TeeDeployer extends SubDeployerSupport implements SubDeployer, TeeDeployerMBean {
	
	
	public TeeDeployer(){
		initializeMainDeployer();
	}
	
	protected void initializeMainDeployer() {
		setSuffixes(new String[]{".tee", "-tee.xml"});
		setRelativeOrder(RELATIVE_ORDER_100);
	}
	/**
	 * Returns true if this deployer can deploy the given DeploymentInfo.
	 *
	 * @return True if this deployer can deploy the given DeploymentInfo.
	 * @jmx:managed-operation
	 */
	public boolean accepts(DeploymentInfo di) {
	    //To be accepted the deployment's root name must end in tee
		String urlStr = di.url.getFile();
		if( !urlStr.endsWith("tee") && !urlStr.endsWith("-tee/") && !urlStr.endsWith("-tee.xml") ) {
			return false;
		} else {
			return true;
		}		  
	}
	
	/**
	 * Describe <code>init</code> method here.
	 *
	 * @param di a <code>DeploymentInfo</code> value
	 * @throws DeploymentException if an error occurs
	 * @jmx:managed-operation
	 */
	public void init(DeploymentInfo di) throws DeploymentException {
		try {
			
			if (di.watch == null) {
				// resolve the watch
				if (di.url.getProtocol().equals("file")) {
					File file = new File(di.url.getFile());
					
					// If not directory we watch the package
					if (!file.isDirectory()) {
						if (di.url.getFile().endsWith(".tee")) {
							di.watch = di.localCl.findResource("META-INF/jboss-tee.xml");
						} else {
							di.watch = di.url;
						}
					} else {
					    //If directory we watch the xml files
						di.watch = new URL(di.url, "META-INF/jboss-tee.xml");
					}
				} else {
					// We watch the top only, no directory support
					di.watch = di.url;
				}
			}
			File file2 = new File(this.tempDeployDir + "/prova.jar");
			file2.mkdir();
			File file3 = new File(this.tempDeployDir + "/prova.jar/META-INF");
			file3.mkdir();
			
			File file4 = new File(this.tempDeployDir + "/prova.jar/META-INF/ejb-jar.xml");
			FileWriter fw = new FileWriter(file4);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
"<!DOCTYPE ejb-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN\" \"http://java.sun.com/dtd/ejb-jar_2_0.dtd\"> " +
"<ejb-jar > " +
"   <enterprise-beans>" +
"      <session >" +
"         <description><![CDATA[]]></description>" +
"         <display-name>WSInterceptor Bean</display-name>" +
"         <ejb-name>WSInterceptor</ejb-name>" +
"         <local-home>it.javalinux.tee.WSinterceptorHome</local-home>" +
"         <local>it.javalinux.tee.WSInterceptor</local>" +
"         <ejb-class>it.javalinux.tee.interceptor.WSInterceptor</ejb-class>" +
"         <session-type>Stateless</session-type>" +
"         <transaction-type>Container</transaction-type>" +
"      </session>" +
"   </enterprise-beans>" +
"   <container-transaction >" +
"      <method >" +
"         <ejb-name>WSInterceptor</ejb-name>" +
"          <method-name>*</method-name>" +
"       </method>" +
"       <trans-attribute>Required</trans-attribute>" +
"    </container-transaction>" +
"</ejb-jar>" );
			fw.flush();
			fw.close();
			
			//JarUtils.jar(new FileOutputStream(file2),file3);
			log.info("AAA");
			log.info(file2.toURL());
			//this.addDeployableJar(di,new JarFile(file2));
			new DeploymentInfo(file2.toURL(), di, getServer());
		}
		catch (Exception e)
		{
			log.error("failed to parse TEE document: ", e);
			throw new DeploymentException(e);
		}
		super.init(di);
	}
	
	private URL getDocUrl(DeploymentInfo di, URLClassLoader localCL) throws DeploymentException
	{
		URL docURL = di.localUrl;
		if (di.isXML == false)
			docURL = localCL.findResource("META-INF/jboss-tee.xml");
		// Validate that the descriptor was found
		if (docURL == null)
			throw new DeploymentException("Failed to find META-INF/jboss-tee.xml");
		return docURL;
	}
	
	/**
	 * Describe <code>create</code> method here.
	 *
	 * @param di a <code>DeploymentInfo</code> value
	 * @throws DeploymentException if an error occurs
	 * @jmx:managed-operation
	 */
	public void create(DeploymentInfo di) throws DeploymentException
	{
		try
		{
			if (!di.isXML)
			{
				Iterator it = ArchiveBrowser.getBrowser(di.url, new ClassFileFilter());
				log.info("BB");
//				AspectAnnotationLoader loader = new AspectAnnotationLoader(AspectManager.instance());
//				loader.deployInputStreamIterator(it);
//				
			}
			
			URL docURL = getDocUrl(di, di.localCl);
			
//			AspectXmlLoader.deployXML(docURL);
			Notification msg = new Notification("TEE Deploy", this, getNextNotificationSequenceNumber());
			sendNotification(msg);
			log.info("Deployed TEE: " + di.url);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new DeploymentException(ex);
		}
	}
	
	/**
	 * The <code>start</code> method starts all the mbeans in this DeploymentInfo..
	 *
	 * @param di a <code>DeploymentInfo</code> value
	 * @throws DeploymentException if an error occurs
	 * @jmx:managed-operation
	 */
	public void start(DeploymentInfo di) throws DeploymentException
	{
	}
	
	/**
	 * Undeploys the package at the url string specified. This will: Undeploy
	 * packages depending on this one. Stop, destroy, and unregister all the
	 * specified mbeans Unload this package and packages this package deployed
	 * via the classpath tag. Keep track of packages depending on this one that
	 * we undeployed so that they can be redeployed should this one be
	 * redeployed.
	 *
	 * @param di the <code>DeploymentInfo</code> value to stop.
	 * @jmx:managed-operation
	 */
	public void stop(DeploymentInfo di)
	//throws DeploymentException
	{
		log.debug("undeploying document " + di.url);
		try
		{
			if (!di.isXML)
			{
				//Iterator it = ArchiveBrowser.getBrowser(di.url, new ClassFileFilter());
//				AspectAnnotationLoader loader = new AspectAnnotationLoader(AspectManager.instance());
//				loader.undeployInputStreamIterator(it);
//				
			}
			
			URL docURL = getDocUrl(di, di.localCl);
			//long start = System.currentTimeMillis();
//			AspectXmlLoader.undeployXML(docURL);
			/*
			 System.out.println("************************");
			 System.out.println("undeploy took: " + (System.currentTimeMillis() - start));
			 System.out.println("************************");
			 */
			Notification msg = new Notification("TEE Undeploy", this, getNextNotificationSequenceNumber());
			sendNotification(msg);
			log.info("UnDeployed TEE: " + di.url);
		}
		catch (Exception ex)
		{
			log.error("failed to stop", ex);
		}
	}
	
	/**
	 * Describe <code>destroy</code> method here.
	 *
	 * @param di a <code>DeploymentInfo</code> value
	 * @jmx:managed-operation
	 */
	public void destroy(DeploymentInfo di)
	//throws DeploymentException
	{
	}
	
	/**
	 * The startService method gets the mbeanProxies for MainDeployer
	 * and ServiceController, used elsewhere.
	 *
	 * @throws Exception if an error occurs
	 */
	protected void startService() throws Exception
	{
		super.startService();
	}
	
	protected ObjectName getObjectName(MBeanServer server, ObjectName name)
	throws MalformedObjectNameException
	{
		return name == null ? OBJECT_NAME : name;
	}
	
}