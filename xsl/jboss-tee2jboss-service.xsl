<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" doctype-system="http://www.jboss.org/j2ee/dtd/jboss-service_3_2.dtd" doctype-public="-//JBoss//DTD MBean Service 3.2//EN" />
  
  <xsl:variable name="teeName" select="Tee/Name"/>
  
  <xsl:template match="Tee">
    <xsl:element name="server">
      <!-- DLQ section -->
      <xsl:element name="mbean">
        <xsl:attribute name="code">org.jboss.mq.server.jmx.Queue</xsl:attribute>
	<xsl:attribute name="name"><xsl:text>jboss.mq.destination:service=Queue,name=</xsl:text><xsl:value-of select="Name"/><xsl:text>DLQ</xsl:text></xsl:attribute>
	<xsl:element name="depends"><xsl:attribute name="optional-attribute-name">DestinationManager</xsl:attribute>jboss.mq:service=DestinationManager</xsl:element>
      </xsl:element>
      <!-- Tee section -->
      <xsl:element name="mbean">
        <xsl:attribute name="code">it.javalinux.tee.mbean.Tee</xsl:attribute>
	<xsl:attribute name="name"><xsl:text>it.javalinux:service=</xsl:text><xsl:value-of select="Name"/></xsl:attribute>
	<xsl:element name="attribute"><xsl:attribute name="name">TeeName</xsl:attribute><xsl:value-of select="Name"/></xsl:element>
      </xsl:element>
      <!-- OAQ section -->
      <xsl:apply-templates select="Interceptors/OAQInterceptor"/>
    </xsl:element>
  </xsl:template>
  
  
  
  <xsl:template match="OAQInterceptor">
    <!-- Variables declaration -->
    <xsl:variable name="queueConnectionFactory">
      <xsl:text>OAQ</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/><xsl:text>QueueConnectionFactory</xsl:text>
    </xsl:variable>
    <xsl:variable name="topicConnectionFactory">
      <xsl:text>OAQ</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/><xsl:text>TopicConnectionFactory</xsl:text>
    </xsl:variable>
    <xsl:variable name="jmsProvider">
      <xsl:text>OAQ</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/><xsl:text>Provider</xsl:text>
    </xsl:variable>
    <xsl:variable name="destinationJndiName">
      <xsl:value-of select="DestinationType"/><xsl:text>/OAQ</xsl:text><xsl:value-of select="$teeName"/><xsl:value-of select="InterceptorID"/>
    </xsl:variable>
    <!-- Provider -->
    <xsl:element name="mbean">
      <xsl:attribute name="code">org.jboss.jms.jndi.JMSProviderLoader</xsl:attribute>
      <xsl:attribute name="name"><xsl:text>jboss.mq:service=JMSProviderLoader,name=</xsl:text><xsl:value-of select="$jmsProvider"/></xsl:attribute>
      <xsl:element name="attribute"><xsl:attribute name="name">ProviderName</xsl:attribute><xsl:value-of select="$jmsProvider"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">ProviderAdapterClass</xsl:attribute>org.jboss.jms.jndi.JNDIProviderAdapter</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">QueueFactoryRef</xsl:attribute><xsl:value-of select="$queueConnectionFactory"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">TopicFactoryRef</xsl:attribute><xsl:value-of select="$topicConnectionFactory"/></xsl:element>
    </xsl:element>
    <!-- Factories -->
    <xsl:element name="mbean">
      <xsl:attribute name="code">jmx.service.oracle.aq.OAQConnectionFactory</xsl:attribute>
      <xsl:attribute name="name"><xsl:text>jmx.service.oracle.aq:service=</xsl:text><xsl:value-of select="$queueConnectionFactory"/></xsl:attribute>
      <xsl:element name="attribute"><xsl:attribute name="name">JndiName</xsl:attribute><xsl:value-of select="$queueConnectionFactory"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">JMSStyle</xsl:attribute>queue</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">IsXA</xsl:attribute>false</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">URL</xsl:attribute><xsl:value-of select="URL"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Username</xsl:attribute><xsl:value-of select="Username"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Password</xsl:attribute><xsl:value-of select="Password"/></xsl:element>
      <xsl:element name="depends">jboss:service=Naming</xsl:element>
    </xsl:element>
    <xsl:element name="mbean">
      <xsl:attribute name="code">jmx.service.oracle.aq.OAQConnectionFactory</xsl:attribute>
      <xsl:attribute name="name"><xsl:text>jmx.service.oracle.aq:service=</xsl:text><xsl:value-of select="$topicConnectionFactory"/></xsl:attribute>
      <xsl:element name="attribute"><xsl:attribute name="name">JndiName</xsl:attribute><xsl:value-of select="$topicConnectionFactory"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">JMSStyle</xsl:attribute>topic</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">IsXA</xsl:attribute>false</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">URL</xsl:attribute><xsl:value-of select="URL"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Username</xsl:attribute><xsl:value-of select="Username"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Password</xsl:attribute><xsl:value-of select="Password"/></xsl:element>
      <xsl:element name="depends">jboss:service=Naming</xsl:element>
    </xsl:element>
    <!-- Destinations -->
    <xsl:element name="mbean">
      <xsl:attribute name="code">jmx.service.oracle.aq.OAQDestination</xsl:attribute>
      <xsl:attribute name="name"><xsl:text>jmx.service.oracle.aq:service=</xsl:text><xsl:value-of select="$destinationJndiName"/></xsl:attribute>
      <xsl:element name="attribute"><xsl:attribute name="name">JndiName</xsl:attribute><xsl:value-of select="$destinationJndiName"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">JMSStyle</xsl:attribute><xsl:value-of select="DestinationType"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">IsXA</xsl:attribute>false</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">URL</xsl:attribute><xsl:value-of select="URL"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Username</xsl:attribute><xsl:value-of select="Username"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Password</xsl:attribute><xsl:value-of select="Password"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">PayloadFactory</xsl:attribute><xsl:value-of select="PayloadFactory"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Schema</xsl:attribute><xsl:value-of select="Schema"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Destination</xsl:attribute><xsl:value-of select="Destination"/></xsl:element>
      <xsl:element name="depends">jboss:service=Naming</xsl:element>
    </xsl:element>
  </xsl:template>
  
  
</xsl:stylesheet>
