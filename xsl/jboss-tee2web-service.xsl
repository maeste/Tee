<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://xml.apache.org/axis/wsdd/" xmlns:tee="http://net.jboss.org/tee" xmlns:java="http://xml.apache.org/axis/wsdd/providers/java" exclude-result-prefixes="xsl">
  <xsl:output method="xml" indent="yes" />
  
  <xsl:variable name="teeName" select="Tee/Name"/>
  <xsl:template match="Tee">
    <xsl:apply-templates select="Interceptors/WSInterceptor"/>
  </xsl:template>
  
  <xsl:template match="WSInterceptor">
    <xsl:element name="deployment">
	    <xsl:attribute name="tee:junk" namespace="http://net.jboss.org/tee">Required because of a Xalan BUG</xsl:attribute>
	    <xsl:attribute name="java:junk" namespace="http://xml.apache.org/axis/wsdd/providers/java">Required because of a Xalan BUG</xsl:attribute>
      <xsl:attribute name="name"><xsl:value-of select="$teeName"/></xsl:attribute>
      <xsl:attribute name="targetNamespace">http://net.jboss.org/tee</xsl:attribute>
      <xsl:element name="service">
        <xsl:attribute name="name"><xsl:value-of select="$teeName"/><xsl:text>.WSInterceptorBean</xsl:text></xsl:attribute>
	<xsl:attribute name="provider">Handler</xsl:attribute>
	<xsl:element name="parameter">
	  <xsl:attribute name="name">handlerClass</xsl:attribute>
	  <xsl:attribute name="value">org.jboss.net.axis.server.EJBProvider</xsl:attribute>
	</xsl:element>
	<xsl:element name="parameter">
	  <xsl:attribute name="name">beanJndiName</xsl:attribute>
	  <xsl:attribute name="value"><xsl:value-of select="$teeName"/><xsl:text>.WSInterceptor</xsl:text></xsl:attribute>
	</xsl:element>
	<xsl:element name="parameter">
	  <xsl:attribute name="name">allowedMethods</xsl:attribute>
	  <xsl:attribute name="value">InterceptMapEvent</xsl:attribute>
	</xsl:element>
	<xsl:element name="requestFlow">
	  <xsl:attribute name="name">WSInterceptorBeanRequest</xsl:attribute>
	</xsl:element>
	<xsl:element name="responseFlow">
	  <xsl:attribute name="name">WSInterceptorBeanResponse</xsl:attribute>
	</xsl:element>
	<xsl:element name="operation">
	  <xsl:attribute name="name">InterceptMapEvent</xsl:attribute>
	  <xsl:attribute name="returnQName">interceptMapEvent</xsl:attribute>
	  <xsl:element name="parameter">
	    <xsl:attribute name="name">mapEvent</xsl:attribute>
	  </xsl:element>
	</xsl:element>
      </xsl:element>
      <xsl:element name="typeMapping">
        <xsl:attribute name="qname">tee:MapEvent</xsl:attribute>
	<xsl:attribute name="type">java:it.javalinux.tee.event.MapEvent</xsl:attribute>
	<xsl:attribute name="serializer">org.jboss.axis.encoding.ser.BeanSerializerFactory</xsl:attribute>
	<xsl:attribute name="deserializer">org.jboss.axis.encoding.ser.BeanDeserializerFactory</xsl:attribute>
	<xsl:attribute name="encodingStyle">http://schemas.xmlsoap.org/soap/encoding/</xsl:attribute>
      </xsl:element>
      
    </xsl:element>
  </xsl:template>
  
</xsl:stylesheet>
