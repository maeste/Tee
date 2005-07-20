<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" doctype-system="http://www.jboss.org/j2ee/dtd/jboss-service_3_2.dtd" doctype-public="-//JBoss//DTD MBean Service 3.2//EN" />
  
  <xsl:template match="Tee">
    <xsl:element name="server">
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
    <xsl:element name="mbean">
      <!-- Factory -->
      <xsl:attribute name="code">jmx.service.oracle.aq.OAQConnectionFactory</xsl:attribute>
      <xsl:attribute name="name"><xsl:text>jmx.service.oracle.aq:service=</xsl:text><xsl:value-of select="QueueConnectionFactory"/></xsl:attribute>
      <xsl:element name="attribute"><xsl:attribute name="name">JndiName</xsl:attribute><xsl:value-of select="QueueConnectionFactory"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">JMSStyle</xsl:attribute>queue</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">IsXA</xsl:attribute>false</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">URL</xsl:attribute><xsl:value-of select="URL"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Username</xsl:attribute><xsl:value-of select="Username"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Password</xsl:attribute><xsl:value-of select="Password"/></xsl:element>
      <xsl:element name="depends">jboss:service=Naming</xsl:element>
    </xsl:element>
    <xsl:element name="mbean">
      <xsl:attribute name="code">jmx.service.oracle.aq.OAQConnectionFactory</xsl:attribute>
      <xsl:attribute name="name"><xsl:text>jmx.service.oracle.aq:service=</xsl:text><xsl:value-of select="TopicConnectionFactory"/></xsl:attribute>
      <xsl:element name="attribute"><xsl:attribute name="name">JndiName</xsl:attribute><xsl:value-of select="TopicConnectionFactory"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">JMSStyle</xsl:attribute>topic</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">IsXA</xsl:attribute>false</xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">URL</xsl:attribute><xsl:value-of select="URL"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Username</xsl:attribute><xsl:value-of select="Username"/></xsl:element>
      <xsl:element name="attribute"><xsl:attribute name="name">Password</xsl:attribute><xsl:value-of select="Password"/></xsl:element>
      <xsl:element name="depends">jboss:service=Naming</xsl:element>
    </xsl:element>
    <!-- Destination -->
    <xsl:element name="mbean">
      <xsl:attribute name="code">jmx.service.oracle.aq.OAQDestination</xsl:attribute>
      <xsl:attribute name="name"><xsl:text>jmx.service.oracle.aq:service=</xsl:text><xsl:value-of select="DestinationJndiName"/></xsl:attribute>
      <xsl:element name="attribute"><xsl:attribute name="name">JndiName</xsl:attribute><xsl:value-of select="DestinationJndiName"/></xsl:element>
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
