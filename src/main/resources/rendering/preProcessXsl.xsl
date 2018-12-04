<?xml version="1.0" encoding="utf-8"?>
<!--
	
	jinFORM
	
	Templates for introducing specific div tags and binding elements to facilitate browser based edition and XML reconstruction
	
	Author: David Dossot
	
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jf="http://jinform.org" xmlns:xd="http://schemas.microsoft.com/office/infopath/2003">
	<xsl:variable name="root_xpath" select="//xsl:template[not(@mode)]/@match"/>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
  <xsl:template match="xsl:output"/>
	<xsl:template match="@xd:binding">
		<xsl:attribute name="jf:binding">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="xsl:template[@mode and @match!=$root_xpath]">
		<xsl:variable name="match" select="@match" />
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<div jf:binding="{$match}" xd:CtrlId="GRP{generate-id()}" jf:type="section">
				<xsl:apply-templates />
			</div>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="body">
		<body>
			<xsl:copy-of select="@*[local-name(.)!='binding']" />
			<div jf:binding="{ancestor::xsl:template/@match}" xd:CtrlId="GRP{generate-id()}" jf:type="section">
				<xsl:apply-templates />
			</div>
		</body>
	</xsl:template>
	<xsl:template match="tbody[@xd:xctname='RepeatingTable']">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:attribute name="jf:repeatable">
				<xsl:text>true</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="jf:binding">
				<xsl:value-of select="xsl:for-each/@select" />
			</xsl:attribute>
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
