<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" doctype-system="http://java.sun.com/dtd/ejb-jar_2_0.dtd" doctype-public="-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN" />
  
  <xsl:variable name="teeName" select="Tee/Name"/>
  
  <xsl:template match="Tee">
    <xsl:element name="ejb-jar">
      <xsl:element name="description">ejb-jar</xsl:element>
      <xsl:element name="display-name">Generated by TeeDeployer</xsl:element>
      <xsl:element name="enterprise-beans">
        <xsl:call-template name="EnterpriseBeans-SessionBeanInterceptor"/>
	<xsl:call-template name="EnterpriseBeans-WSInterceptor"/>
	<xsl:call-template name="EnterpriseBeans-JMSInterceptor"/>
	<xsl:call-template name="EnterpriseBeans-OAQInterceptor"/>
      </xsl:element>
      <xsl:call-template name="ContainerTransaction-SessionBeanInterceptor"/>
      <xsl:call-template name="ContainerTransaction-WSInterceptor"/>
      <xsl:call-template name="ContainerTransaction-JMSInterceptor"/>
      <xsl:call-template name="ContainerTransaction-OAQInterceptor"/>
    </xsl:element>
  </xsl:template>
  
  
  <xsl:template name="EnterpriseBeans-SessionBeanInterceptor">
    <xsl:for-each select="Interceptors/SessionBeanInterceptor">
      <xsl:element name="session">
        <xsl:element name="display-name"><xsl:value-of select="EjbName"/></xsl:element>
        <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
	<xsl:variable name="ejbType" select="EjbType"/>
	<xsl:if test="$ejbType = 'local'">
	  <xsl:element name="local-home">it.javalinux.tee.interceptor.SessionBeanInterceptorLocalHome</xsl:element>
          <xsl:element name="local">it.javalinux.tee.interceptor.SessionBeanInterceptorLocal</xsl:element>
	</xsl:if>
	<xsl:if test="$ejbType = 'remote'">
	  <xsl:element name="home">it.javalinux.tee.interceptor.SessionBeanInterceptorRemoteHome</xsl:element>
          <xsl:element name="remote">it.javalinux.tee.interceptor.SessionBeanInterceptorRemote</xsl:element>
	</xsl:if>
        <xsl:element name="ejb-class">it.javalinux.tee.interceptor.SessionBeanInterceptorBean</xsl:element>
        <xsl:element name="session-type">Stateless</xsl:element>
        <xsl:element name="transaction-type">Container</xsl:element>
	<xsl:element name="env-entry">
	  <xsl:element name="env-entry-name">teeName</xsl:element>
	  <xsl:element name="env-entry-type">java.lang.String</xsl:element>
	  <xsl:element name="env-entry-value"><xsl:value-of select="$teeName"/></xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="EnterpriseBeans-WSInterceptor">
    <xsl:for-each select="Interceptors/WSInterceptor">
      <xsl:element name="session">
        <xsl:element name="display-name">WSInterceptor Bean</xsl:element>
        <xsl:element name="ejb-name">WSInterceptorBean</xsl:element>
        <xsl:element name="local-home">it.javalinux.tee.interceptor.WSInterceptorHome</xsl:element>
        <xsl:element name="local">it.javalinux.tee.interceptor.WSInterceptor</xsl:element>
        <xsl:element name="ejb-class">it.javalinux.tee.interceptor.WSInterceptorBean</xsl:element>
        <xsl:element name="session-type">Stateless</xsl:element>
        <xsl:element name="transaction-type">Container</xsl:element>
	<xsl:element name="env-entry">
	  <xsl:element name="env-entry-name">teeName</xsl:element>
	  <xsl:element name="env-entry-type">java.lang.String</xsl:element>
	  <xsl:element name="env-entry-value"><xsl:value-of select="$teeName"/></xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="EnterpriseBeans-JMSInterceptor">
    <xsl:for-each select="Interceptors/JMSInterceptor">
      <xsl:element name="message-driven">
        <xsl:element name="display-name"><xsl:value-of select="EjbName"/></xsl:element>
        <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
        <xsl:element name="ejb-class">it.javalinux.tee.interceptor.WSInterceptorBean</xsl:element>
	<xsl:choose>
	  <xsl:when test="AcknowledgeMode">
	    <xsl:element name="acknowledge-mode"><xsl:value-of select="AcknowledgeMode"/></xsl:element>
	  </xsl:when>
	  <xsl:otherwise>
	    <xsl:element name="acknowledge-mode">Auto-acknowledge</xsl:element>
	  </xsl:otherwise>
	</xsl:choose>
        <xsl:element name="message-driven-destination">
	  <xsl:element name="destination-type">
	    <xsl:variable name="destinationType" select="DestinationType"/>
	    <xsl:if test="$destinationType = 'queue'">
	      <xsl:text>javax.jms.Queue</xsl:text>
	    </xsl:if>
	    <xsl:if test="$destinationType = 'topic'">
	      <xsl:text>javax.jms.Topic</xsl:text>
	    </xsl:if>
	  </xsl:element>
	</xsl:element>
	<xsl:element name="transaction-type">Container</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="EnterpriseBeans-OAQInterceptor">
    <xsl:for-each select="Interceptors/OAQInterceptor">
      <xsl:element name="message-driven">
        <xsl:element name="display-name"><xsl:value-of select="EjbName"/></xsl:element>
        <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
        <xsl:element name="ejb-class">it.javalinux.tee.interceptor.WSInterceptorBean</xsl:element>
	<xsl:choose>
	  <xsl:when test="AcknowledgeMode">
	    <xsl:element name="acknowledge-mode"><xsl:value-of select="AcknowledgeMode"/></xsl:element>
	  </xsl:when>
	  <xsl:otherwise>
	    <xsl:element name="acknowledge-mode">Auto-acknowledge</xsl:element>
	  </xsl:otherwise>
	</xsl:choose>
        <xsl:element name="message-driven-destination">
	  <xsl:element name="destination-type">
	    <xsl:variable name="destinationType" select="DestinationType"/>
	    <xsl:if test="$destinationType = 'queue'">
	      <xsl:text>javax.jms.Queue</xsl:text>
	    </xsl:if>
	    <xsl:if test="$destinationType = 'topic'">
	      <xsl:text>javax.jms.Topic</xsl:text>
	    </xsl:if>
	  </xsl:element>
	</xsl:element>
	<xsl:element name="transaction-type">Container</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="ContainerTransaction-SessionBeanInterceptor">
    <xsl:for-each select="Interceptors/SessionBeanInterceptor">
      <xsl:element name="container-transaction">
        <xsl:element name="method">
	  <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
	  <xsl:element name="method-name">*</xsl:element>
	</xsl:element>
	<xsl:choose>
	  <xsl:when test="TransactionType">
	    <xsl:element name="trans-attribute"><xsl:value-of select="TransactionType"/></xsl:element>
	  </xsl:when>
	  <xsl:otherwise>
	    <xsl:element name="trans-attribute">Required</xsl:element>
	  </xsl:otherwise>
	</xsl:choose>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="ContainerTransaction-WSInterceptor">
    <xsl:for-each select="Interceptors/WSInterceptor">
      <xsl:element name="container-transaction">
        <xsl:element name="method">
	  <xsl:element name="ejb-name">WSInterceptorBean</xsl:element>
	  <xsl:element name="method-name">*</xsl:element>
	</xsl:element>
        <xsl:element name="trans-attribute">Required</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="ContainerTransaction-JMSInterceptor">
    <xsl:for-each select="Interceptors/JMSInterceptor">
      <xsl:element name="container-transaction">
        <xsl:element name="method">
	  <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
	  <xsl:element name="method-name">*</xsl:element>
	</xsl:element>
	<xsl:element name="trans-attribute">Required</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="ContainerTransaction-OAQInterceptor">
    <xsl:for-each select="Interceptors/OAQInterceptor">
      <xsl:element name="container-transaction">
        <xsl:element name="method">
	  <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
	  <xsl:element name="method-name">*</xsl:element>
	</xsl:element>
	<xsl:element name="trans-attribute">Required</xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
</xsl:stylesheet>