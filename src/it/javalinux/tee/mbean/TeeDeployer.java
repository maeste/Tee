/*
 * Created on 10-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.deployment.SubDeployer;
import org.jboss.deployment.SubDeployerSupport;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.logging.Logger;
import org.jboss.util.file.ArchiveBrowser;
import org.jboss.util.file.ClassFileFilter;
import org.w3c.dom.Document;

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
			//jboss-tee.xml parsing...
			Document jbossTeeDocument = this.readDocument(new File(di.watch.getFile()));
			//TODO! Inserire controlli: validazione, cose mancanti (tipo uno potrebbe non avere il webservice)
			
			//ejb-jar.xml
			File jar = new File(this.tempDeployDir + "/jboss-tee-dep.jar");
			jar.mkdir();
			File jarMetaInf = new File(jar,"META-INF");
			jarMetaInf.mkdir();
			File ejbJarXml = new File(jarMetaInf,"ejb-jar.xml");
			ejbJarXml.createNewFile();
			this.applyXsl("/jboss-tee2ejb-jar.xsl", jbossTeeDocument, ejbJarXml);
			log.info("generated file: "+ejbJarXml.getAbsolutePath());
			
			//jboss.xml
			File jbossXml = new File(jarMetaInf,"jboss.xml");
			this.applyXsl("/jboss-tee2jboss.xsl", jbossTeeDocument, jbossXml);
			log.info("generated file: "+jbossXml.getAbsolutePath());
			
			new DeploymentInfo(jar.toURL(), di, getServer());
			
			//jboss-service.xml
			File sar = new File(this.tempDeployDir + "/jboss-tee-dep.sar");
			sar.mkdir();
			File sarMetaInf = new File(sar,"META-INF");
			sarMetaInf.mkdir();
			File jbossServiceXml = new File(sarMetaInf,"jboss-service.xml");
			this.applyXsl("/jboss-tee2jboss-service.xsl", jbossTeeDocument, jbossServiceXml);
			log.info("generated file: "+jbossServiceXml.getAbsolutePath());
			new DeploymentInfo(jbossServiceXml.toURL(), di, getServer());
			
			//web-service.xml
			File wsr = new File(this.tempDeployDir + "/jboss-tee-dep.wsr");
			wsr.mkdir();
			File wsrMetaInf = new File(wsr,"META-INF");
			wsrMetaInf.mkdir();
			File webServiceXml = new File(wsrMetaInf,"web-service.xml");
			this.applyXsl("/jboss-tee2web-service.xsl", jbossTeeDocument, webServiceXml);
			log.info("generated file: "+webServiceXml.getAbsolutePath());
	//		new DeploymentInfo(wsr.toURL(), di, getServer()); //TODO!! Ripristinare per i WS...
			
			//eventualmente qui si può decidere di scompattare/copiare i jar contenuti...
			//se mettiamo assieme gli eventi (nel caso fare come nell'EARDeployer, con extractNestedJar)
						
			//JarUtils.jar(new FileOutputStream(file2),file3);
			
			//this.addDeployableJar(di,new JarFile(file2));
		} catch (Exception e) {
			log.error("Failed to parse TEE document: ", e);
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
	public void start(DeploymentInfo di) throws DeploymentException {
		log.info("start called...");
		RMIAdaptor rmiserver = null;
        try {
			Properties prop = new Properties();
	        prop.put( "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" );
	        prop.put( "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" );
	        prop.put( "java.naming.provider.url", "jnp://localhost:1099");
	        InitialContext ctx = new InitialContext(prop);
	        Logger.getLogger(this.getClass()).debug("Looking up RMI adaptor...");
            rmiserver = (RMIAdaptor) ctx.lookup("jmx/invoker/RMIAdaptor");
            if( rmiserver == null ) Logger.getLogger(this.getClass()).debug( "RMIAdaptor is null");
            ObjectName teeOName = new ObjectName("it.javalinux:service=TeeLince"); //TODO!!!
            Object[] parArray = {di.watch.getFile()};
            System.out.println("watch:"+di.watch.getFile()); //TODO è sempre giusto? provare nel caso di jar
            String[] signArray = {"java.lang.String"};
            Logger.getLogger(this.getClass()).debug("Invoking service...");
            rmiserver.invoke(teeOName,"setSpecificationURLString",parArray,signArray);
            rmiserver.invoke(teeOName,"readSpecification",null,null);
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("Error calling Tee service!");
			StringWriter sw = new StringWriter();
    		e.printStackTrace(new PrintWriter(sw));
    		Logger.getLogger(this.getClass()).error(sw.toString());
		}
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
		log.info("destroy called...");
	}
	
	/**
	 * The startService method gets the mbeanProxies for MainDeployer
	 * and ServiceController, used elsewhere.
	 *
	 * @throws Exception if an error occurs
	 */
	protected void startService() throws Exception
	{
		log.info("startService called...");
		super.startService();
	}
	
	protected ObjectName getObjectName(MBeanServer server, ObjectName name)
	throws MalformedObjectNameException
	{
		return name == null ? OBJECT_NAME : name;
	}
	
	private Document readDocument(File file) throws Exception  {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().parse(file);
	}
	
	private boolean validate(File xmlFile, String xsdPath) {
		return true; //TODO!!!
	}
	
	
	private void applyXsl(String xslName, Document document, File destFile) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		InputStream is = Tee.class.getResourceAsStream(xslName); //is there a better way?
		Templates templ = factory.newTemplates(new StreamSource(is));
		Transformer transformer = templ.newTransformer();
		DOMSource source = new DOMSource(document);
		FileWriter writer = new FileWriter(destFile);
		StreamResult result = new StreamResult(writer);
		transformer.setOutputProperties(templ.getOutputProperties()); //to emit attributes of tag <xsl:output>
		transformer.transform(source, result);
		writer.flush();
		writer.close();
	}
	
	
}