<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" doctype-system="http://www.jboss.org/j2ee/dtd/jboss_3_2.dtd" doctype-public="-//JBoss//DTD JBOSS 3.2//EN" />
  
  <xsl:variable name="teeName" select="Tee/Name"/>
  
  <xsl:template match="Tee">
    <xsl:element name="jboss">
      <xsl:element name="enterprise-beans">
        <xsl:call-template name="EnterpriseBeans-SessionBeanInterceptor"/>
        <xsl:call-template name="EnterpriseBeans-WSInterceptor"/>
        <xsl:call-template name="EnterpriseBeans-JMSInterceptor"/>
        <xsl:call-template name="EnterpriseBeans-OAQInterceptor"/>
      </xsl:element>
      <xsl:element name="resource-managers"/>
      <xsl:element name="invoker-proxy-bindings">
        <xsl:call-template name="InvokerProxyBindings-JMSInterceptor"/>
	<xsl:call-template name="InvokerProxyBindings-OAQInterceptor"/>
      </xsl:element>
      <xsl:element name="container-configurations">
        <xsl:call-template name="ContainerConfigurations-JMSInterceptor"/>
	<xsl:call-template name="ContainerConfigurations-OAQInterceptor"/>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  
  <xsl:template name="ContainerConfigurations-JMSInterceptor">
    <xsl:for-each select="Interceptors/JMSInterceptor">
      <!-- Variables declaration -->
      <xsl:variable name="invokerProxyBindingName">
        <xsl:text>jms-</xsl:text><xsl:value-of select="$teeName"/><xsl:text>-</xsl:text><xsl:value-of select="InterceptorID"/><xsl:text>-message-driven-bean</xsl:text>
      </xsl:variable>
      <xsl:variable name="containerConfigurationName">
        <xsl:text>JMS </xsl:text><xsl:value-of select="$teeName"/><xsl:text> </xsl:text><xsl:value-of select="InterceptorID"/><xsl:text> Message Driven Bean</xsl:text>
      </xsl:variable>
      <!-- Element generation... -->
      <xsl:element name="container-configuration">
        <xsl:element name="container-name"><xsl:value-of select="$containerConfigurationName"/></xsl:element>
	<xsl:element name="call-logging">false</xsl:element>
	<xsl:element name="invoker-proxy-binding-name"><xsl:value-of select="$invokerProxyBindingName"/></xsl:element>
	<xsl:element name="container-interceptors">
	  <xsl:element name="interceptor">org.jboss.ejb.plugins.ProxyFactoryFinderInterceptor</xsl:element>
	  <xsl:element name="interceptor">org.jboss.ejb.plugins.LogInterceptor</xsl:element>
	  <xsl:element name="interceptor">org.jboss.ejb.plugins.RunAsSecurityInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Container</xsl:attribute>org.jboss.ejb.plugins.TxInterceptorCMT</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Container</xsl:attribute><xsl:attribute name="metricsEnabled">true</xsl:attribute>org.jboss.ejb.plugins.MetricsInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Container</xsl:attribute>org.jboss.ejb.plugins.MessageDrivenInstanceInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Bean</xsl:attribute>org.jboss.ejb.plugins.MessageDrivenInstanceInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Bean</xsl:attribute>org.jboss.ejb.plugins.MessageDrivenTxInterceptorBMT</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Bean</xsl:attribute><xsl:attribute name="metricsEnabled">true</xsl:attribute>org.jboss.ejb.plugins.MetricsInterceptor</xsl:element>
	  <xsl:element name="interceptor">org.jboss.resource.connectionmanager.CachedConnectionInterceptor</xsl:element>
	</xsl:element>
	<xsl:element name="instance-pool">org.jboss.ejb.plugins.MessageDrivenInstancePool</xsl:element>
	<xsl:element name="instance-cache"></xsl:element>
	<xsl:element name="persistence-manager"></xsl:element>
	<xsl:element name="container-pool-conf">
	  <xsl:element name="MaximumSize">100</xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  
  <xsl:template name="ContainerConfigurations-OAQInterceptor">
    <xsl:for-each select="Interceptors/OAQInterceptor">
      <!-- Variables declaration -->
      <xsl:variable name="invokerProxyBindingName">
        <xsl:text>oaq-</xsl:text><xsl:value-of select="$teeName"/><xsl:text>-</xsl:text><xsl:value-of select="InterceptorID"/><xsl:text>-message-driven-bean</xsl:text>
      </xsl:variable>
      <xsl:variable name="containerConfigurationName">
        <xsl:text>OAQ </xsl:text><xsl:value-of select="$teeName"/><xsl:text> </xsl:text><xsl:value-of select="InterceptorID"/><xsl:text> Message Driven Bean</xsl:text>
      </xsl:variable>
      <!-- Element generation... -->
      <xsl:element name="container-configuration">
        <xsl:element name="container-name"><xsl:value-of select="$containerConfigurationName"/></xsl:element>
	<xsl:element name="call-logging">false</xsl:element>
	<xsl:element name="invoker-proxy-binding-name"><xsl:value-of select="$invokerProxyBindingName"/></xsl:element>
	<xsl:element name="container-interceptors">
	  <xsl:element name="interceptor">org.jboss.ejb.plugins.ProxyFactoryFinderInterceptor</xsl:element>
	  <xsl:element name="interceptor">org.jboss.ejb.plugins.LogInterceptor</xsl:element>
	  <xsl:element name="interceptor">org.jboss.ejb.plugins.RunAsSecurityInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Container</xsl:attribute>org.jboss.ejb.plugins.TxInterceptorCMT</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Container</xsl:attribute><xsl:attribute name="metricsEnabled">true</xsl:attribute>org.jboss.ejb.plugins.MetricsInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Container</xsl:attribute>org.jboss.ejb.plugins.MessageDrivenInstanceInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Bean</xsl:attribute>org.jboss.ejb.plugins.MessageDrivenInstanceInterceptor</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Bean</xsl:attribute>org.jboss.ejb.plugins.MessageDrivenTxInterceptorBMT</xsl:element>
	  <xsl:element name="interceptor"><xsl:attribute name="transaction">Bean</xsl:attribute><xsl:attribute name="metricsEnabled">true</xsl:attribute>org.jboss.ejb.plugins.MetricsInterceptor</xsl:element>
	  <xsl:element name="interceptor">org.jboss.resource.connectionmanager.CachedConnectionInterceptor</xsl:element>
	</xsl:element>
	<xsl:element name="instance-pool">org.jboss.ejb.plugins.MessageDrivenInstancePool</xsl:element>
	<xsl:element name="instance-cache"></xsl:element>
	<xsl:element name="persistence-manager"></xsl:element>
	<xsl:element name="container-pool-conf">
	  <xsl:element name="MaximumSize">100</xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  
  <xsl:template name="InvokerProxyBindings-JMSInterceptor">
    <xsl:for-each select="Interceptors/JMSInterceptor">
      <!-- Variables declaration -->
      <xsl:variable name="invokerProxyBindingName">
        <xsl:text>jms-</xsl:text><xsl:value-of select="$teeName"/><xsl:text>-</xsl:text><xsl:value-of select="InterceptorID"/><xsl:text>-message-driven-bean</xsl:text>
      </xsl:variable>
      <xsl:variable name="jmsProvider">
        <xsl:text>JMS</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/><xsl:text>Provider</xsl:text>
      </xsl:variable>
      <!-- Element generation... -->
      <xsl:element name="invoker-proxy-binding">
        <xsl:element name="name">
	  <xsl:value-of select="$invokerProxyBindingName"/>
	</xsl:element>
	<xsl:element name="invoker-mbean">default</xsl:element>
	<xsl:element name="proxy-factory">org.jboss.ejb.plugins.jms.JMSContainerInvoker</xsl:element>
	<xsl:element name="proxy-factory-config">
	  <xsl:element name="JMSProviderAdapterJNDI"><xsl:value-of select="$jmsProvider"/></xsl:element>
	  <xsl:element name="ServerSessionPoolFactoryJNDI">StdJMSPool</xsl:element>
	  <xsl:element name="MaximumSize">1</xsl:element>
	  <xsl:element name="MaxMessages">1</xsl:element>
	  <xsl:element name="MDBConfig">
	    <xsl:element name="ReconnectIntervalSec">10</xsl:element>
	    <xsl:element name="DLQConfig">
	      <xsl:element name="DestinationQueue">queue/DLQ</xsl:element>
	      <xsl:element name="MaxTimesRedelivered">10</xsl:element>
	      <xsl:element name="TimeToLive">0</xsl:element>
	    </xsl:element>
	  </xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  
  <xsl:template name="InvokerProxyBindings-OAQInterceptor">
    <xsl:for-each select="Interceptors/OAQInterceptor">
      <!-- Variables declaration -->
      <xsl:variable name="invokerProxyBindingName">
        <xsl:text>oaq-</xsl:text><xsl:value-of select="$teeName"/><xsl:text>-</xsl:text><xsl:value-of select="InterceptorID"/><xsl:text>-message-driven-bean</xsl:text>
      </xsl:variable>
      <xsl:variable name="jmsProvider">
        <xsl:text>OAQ</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/><xsl:text>Provider</xsl:text>
      </xsl:variable>
      <!-- Element generation... -->
      <xsl:element name="invoker-proxy-binding">
        <xsl:element name="name">
	  <xsl:value-of select="$invokerProxyBindingName"/>
	</xsl:element>
	<xsl:element name="invoker-mbean">default</xsl:element>
	<xsl:element name="proxy-factory">org.jboss.ejb.plugins.jms.JMSContainerInvoker</xsl:element>
	<xsl:element name="proxy-factory-config">
	  <xsl:element name="JMSProviderAdapterJNDI"><xsl:value-of select="$jmsProvider"/></xsl:element>
	  <xsl:element name="ServerSessionPoolFactoryJNDI">StdJMSPool</xsl:element>
	  <xsl:element name="MaximumSize">1</xsl:element>
	  <xsl:element name="MaxMessages">1</xsl:element>
	  <xsl:element name="MDBConfig">
	    <xsl:element name="ReconnectIntervalSec">10</xsl:element>
	    <xsl:element name="DLQConfig">
	      <xsl:element name="DestinationQueue">queue/DLQ</xsl:element>
	      <xsl:element name="MaxTimesRedelivered">10</xsl:element>
	      <xsl:element name="TimeToLive">0</xsl:element>
	    </xsl:element>
	  </xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  
  <xsl:template name="EnterpriseBeans-SessionBeanInterceptor">
    <xsl:for-each select="Interceptors/SessionBeanInterceptor">
      <xsl:element name="session">
        <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
	<xsl:variable name="ejbType" select="EjbType"/>
	<xsl:if test="$ejbType = 'local'">
	  <xsl:element name="local-jndi-name"><xsl:value-of select="JndiName"/></xsl:element>
	</xsl:if>
	<xsl:if test="$ejbType = 'remote'">
          <xsl:element name="jndi-name"><xsl:value-of select="JndiName"/></xsl:element>
	</xsl:if>
        <xsl:element name="method-attributes"/>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="EnterpriseBeans-WSInterceptor">
    <xsl:for-each select="Interceptors/WSInterceptor">
      <xsl:element name="session">
        <xsl:element name="ejb-name">WSInterceptorBean</xsl:element>
        <xsl:element name="local-jndi-name"><xsl:value-of select="$teeName"/><xsl:text>.WSInterceptor</xsl:text></xsl:element>
	<xsl:element name="method-attributes"/>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="EnterpriseBeans-JMSInterceptor">
    <xsl:for-each select="Interceptors/JMSInterceptor">
      <!-- Variables definition -->
      <xsl:variable name="ejbName">
        <xsl:text>JMS</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/><xsl:text>MessageBean</xsl:text>
      </xsl:variable>
      <xsl:variable name="containerConfigurationName">
        <xsl:text>JMS </xsl:text><xsl:value-of select="$teeName"/><xsl:text> </xsl:text><xsl:value-of select="InterceptorID"/><xsl:text> Message Driven Bean</xsl:text>
      </xsl:variable>
      <xsl:variable name="destinationJndiName">
        <xsl:value-of select="DestinationType"/><xsl:text>/JMS</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/>
      </xsl:variable>
      <!-- Element generation... -->
      <xsl:element name="message-driven">
        <xsl:element name="ejb-name"><xsl:value-of select="$ejbName"/></xsl:element>
	<xsl:element name="destination-jndi-name"><xsl:value-of select="$destinationJndiName"/></xsl:element>
	<xsl:element name="configuration-name"><xsl:value-of select="$containerConfigurationName"/></xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="EnterpriseBeans-OAQInterceptor">
    <xsl:for-each select="Interceptors/OAQInterceptor">
      <!-- Variables definition -->
      <xsl:variable name="ejbName">
        <xsl:text>OAQ</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/><xsl:text>MessageBean</xsl:text>
      </xsl:variable>
      <xsl:variable name="containerConfigurationName">
        <xsl:text>OAQ </xsl:text><xsl:value-of select="$teeName"/><xsl:text> </xsl:text><xsl:value-of select="InterceptorID"/><xsl:text> Message Driven Bean</xsl:text>
      </xsl:variable>
      <xsl:variable name="destinationJndiName">
        <xsl:value-of select="DestinationType"/><xsl:text>/OAQ</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/>
      </xsl:variable>
      <!-- Element generation... -->
      <xsl:element name="message-driven">
        <xsl:element name="ejb-name"><xsl:value-of select="$ejbName"/></xsl:element>
	<xsl:element name="destination-jndi-name"><xsl:value-of select="$destinationJndiName"/></xsl:element>
	<xsl:element name="configuration-name"><xsl:value-of select="$containerConfigurationName"/></xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  
</xsl:stylesheet>