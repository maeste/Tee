/*
 * Created on 10-lug-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package it.javalinux.tee.mbean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
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
import org.jboss.util.file.ArchiveBrowser;
import org.jboss.util.file.ClassFileFilter;
import org.jboss.util.file.JarUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
            URL myUrl = di.url;
			// resolve the watch
			if (di.url.getProtocol().equals("file")) {
				File file = new File(di.url.getFile());
				
				// If not directory we watch the package
				if (!file.isDirectory()) {
					if (di.url.getFile().endsWith(".tee")) {
						di.watch = di.localCl.findResource("META-INF/jboss-tee.xml");
						myUrl = JarUtils.extractNestedJar(di.watch, this.tempDeployDir);
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
			
			//jboss-tee.xml parsing...
			Document jbossTeeDocument = this.readDocument(new File(myUrl.getFile()));
//			Document jbossTeeDocument = this.readDocumentWithValidation(new File(di.watch.getFile()),"/jboss-tee.xsd");
			
			NodeList sessionBeanIntercNodeList = jbossTeeDocument.getElementsByTagName("SessionBeanInterceptor");
			NodeList wsIntercNodeList = jbossTeeDocument.getElementsByTagName("WSInterceptor");
			NodeList jmsIntercNodeList = jbossTeeDocument.getElementsByTagName("JMSInterceptor");
			NodeList oaqIntercNodeList = jbossTeeDocument.getElementsByTagName("OAQInterceptor");

			String teeName = null;
			NodeList nl = jbossTeeDocument.getElementsByTagName("Name");
			for (int i = 0; i < nl.getLength(); i++) {
			    Node node = nl.item(i);
			    if (node.getParentNode().getNodeName().equalsIgnoreCase("Tee")) {
			        teeName = node.getFirstChild().getNodeValue();
			    }
			}
			String tempDeploySubDirPrefix = this.tempDeployDir+"/"+teeName+"-dep";
			File specificationFile = new File(tempDeploySubDirPrefix+".xml");
			specificationFile.createNewFile();
			this.writeDocument(jbossTeeDocument, specificationFile);
			
			
			if (sessionBeanIntercNodeList.getLength()>0 || wsIntercNodeList.getLength()>0 ||
			        jmsIntercNodeList.getLength()>0 || oaqIntercNodeList.getLength()>0) {
			    //there's at least one interceptor -> ejb-jar.xml & jboss.xml required
			    
			    //ejb-jar.xml
			    File jar = new File(tempDeploySubDirPrefix + ".jar");
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
			}
			
			//jboss-service.xml
			File sar = new File(tempDeploySubDirPrefix + ".sar");
			sar.mkdir();
			File sarMetaInf = new File(sar,"META-INF");
			sarMetaInf.mkdir();
			File jbossServiceXml = new File(sarMetaInf,"jboss-service.xml");
			this.applyXsl("/jboss-tee2jboss-service.xsl", jbossTeeDocument, jbossServiceXml);
			log.info("generated file: "+jbossServiceXml.getAbsolutePath());
			
			new DeploymentInfo(jbossServiceXml.toURL(), di, getServer());
			
			if (wsIntercNodeList.getLength()>0) {
			    //there's a WSInterceptor -> web-service.xml required
			    
				//web-service.xml
				File wsr = new File(tempDeploySubDirPrefix + ".wsr");
				wsr.mkdir();
				File wsrMetaInf = new File(wsr,"META-INF");
				wsrMetaInf.mkdir();
				File webServiceXml = new File(wsrMetaInf,"web-service.xml");
				this.applyXsl("/jboss-tee2web-service.xsl", jbossTeeDocument, webServiceXml);
				log.info("generated file: "+webServiceXml.getAbsolutePath());
				new DeploymentInfo(wsr.toURL(), di, getServer());
			}
			
			//deploy nested jars (containing events for example)
			File parentDir = null;
			HashMap extractedJars = new HashMap();
			if (di.isDirectory) {
				parentDir = new File(di.localUrl.getFile());
			} else if (di.localUrl.getFile().endsWith(".tee")) {
				String urlPrefix = "jar:"+di.localUrl+"!/";
				JarFile jarFile = new JarFile(di.localUrl.getFile());
				for (Enumeration e = jarFile.entries(); e.hasMoreElements(); ) {
					JarEntry entry = (JarEntry)e.nextElement();
					String name = entry.getName();
					try {
						URL url = new URL(urlPrefix+name);
						if (isDeployable(name, url)) {
							URL nestedURL = JarUtils.extractNestedJar(url, this.tempDeployDir);
							extractedJars.put(name, nestedURL);
							log.debug("Extracted deployable content: "+name);
							new DeploymentInfo(nestedURL, di, getServer());
						} else if (entry.isDirectory()==false) {
							JarUtils.extractNestedJar(url, this.tempDeployDir);
							log.debug("Extracted non-deployable content: "+name);
						}
					} catch (MalformedURLException mue) {
						log.warn("Jar entry invalid. Ignoring: "+name, mue);
					} catch (IOException ex) {
						log.warn("Failed to extract nested jar. Ignoring: "+name, ex);
					}
				}
			}
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
	public void create(DeploymentInfo di) throws DeploymentException {
		try {
			if (!di.isXML) {
				Iterator it = ArchiveBrowser.getBrowser(di.url, new ClassFileFilter());
//				AspectAnnotationLoader loader = new AspectAnnotationLoader(AspectManager.instance());
//				loader.deployInputStreamIterator(it);
//				
			}
			
			URL docURL = getDocUrl(di, di.localCl);
			
//			AspectXmlLoader.deployXML(docURL);
			Notification msg = new Notification("TEE Deploy", this, getNextNotificationSequenceNumber());
			sendNotification(msg);
			log.info("Deployed TEE: " + di.url);
		} catch (Exception ex) 	{
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
	protected void startService() throws Exception {
		log.info("startService called...");
		super.startService();
	}
	
	
	protected ObjectName getObjectName(MBeanServer server, ObjectName name) throws MalformedObjectNameException {
		return name == null ? OBJECT_NAME : name;
	}
	
	/**
	 * Writes a org.w3c.dom.Document to the specified file, including doctypes.
	 * 
	 * @param document
	 * @param file
	 * @throws Exception
	 */
	private void writeDocument(Document document, File file) throws Exception {
	    Transformer t = TransformerFactory.newInstance().newTransformer();
	    t.setOutputProperty(OutputKeys.INDENT, "yes");
	    t.setOutputProperty(OutputKeys.METHOD, "xml");
	    t.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
	    if (document.getDoctype()!=null) {
		    t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, document.getDoctype().getPublicId());
		    t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId());
	    }
	    FileWriter writer = new FileWriter(file);
		StreamResult result = new StreamResult(writer);
	    t.transform(new DOMSource(document), result);
	    writer.flush();
	    writer.close();
	}
	
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private Document readDocument(File file) throws Exception  {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().parse(file);
	}
	
	/**
	 * 
	 * @param file
	 * @param xsdPath
	 * @return
	 * @throws Exception
	 */
	private Document readDocumentWithValidation(File file, String xsdPath) throws Exception {
		//TODO!!! Ancora da testare
		String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		try {
		    URL url = Tee.class.getResource(xsdPath);
		    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		    //factory.setAttribute(JAXP_SCHEMA_SOURCE, new File(url.getFile()));
		    //factory.setAttribute(JAXP_SCHEMA_SOURCE, url.getFile());
		    factory.setAttribute(JAXP_SCHEMA_SOURCE, "resource:jboss-tee.xsd"); //TODO Così lo trova... ma va sistemato
		    return factory.newDocumentBuilder().parse(file);
		} catch (Exception e) {
		    log.info("Cannot validate and/or parse the document!");
		    throw e;
		}
	}
	
	
	/**
	 * Transforms an org.w3c.dom.Document using the specified XSL and writes the result to disk
	 * 
	 * @param xslName
	 * @param document
	 * @param destFile
	 * @throws Exception
	 */
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