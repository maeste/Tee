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
    </xsl:element>
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
      <xsl:element name="message-driven">
        <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
	<xsl:element name="destination-jndi-name"><xsl:value-of select="DestinationJndiName"/></xsl:element>
	<xsl:element name="configuration-name"><xsl:value-of select="ContainerConfigurationName"/></xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="EnterpriseBeans-OAQInterceptor">
    <xsl:for-each select="Interceptors/OAQInterceptor">
      <xsl:element name="message-driven">
        <xsl:element name="ejb-name"><xsl:value-of select="EjbName"/></xsl:element>
	<xsl:element name="destination-jndi-name"><xsl:value-of select="DestinationJndiName"/></xsl:element>
	<xsl:element name="configuration-name"><xsl:value-of select="ContainerConfigurationName"/></xsl:element>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>
  
  
</xsl:stylesheet>