<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:jf="http://jinform.org" xmlns:xd="http://schemas.microsoft.com/office/infopath/2003" xmlns:ev="http://www.w3.org/2001/xml-events">

  <xsl:output method="xml" omit-xml-declaration="yes" />

  <xsl:param name="generator_name" />
  <xsl:param name="form_name" />
  <xsl:param name="server_root" />

  <xsl:variable name="infopath_resource_prefix">res://infopath.exe/</xsl:variable>

  <xsl:variable name="server_resource_url">
    <xsl:value-of select="$server_root" />
    <xsl:text>/cd/resource</xsl:text>
  </xsl:variable>
  <xsl:variable name="server_image_url">
    <xsl:value-of select="$server_root" />
    <xsl:text>/images</xsl:text>
  </xsl:variable>
  <xsl:variable name="server_image_resource_url">
    <xsl:value-of select="$server_image_url" />
    <xsl:text>resources</xsl:text>
  </xsl:variable>

  <!-- Identity transform -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>

  <!-- Named templates -->
  <xsl:template name="build-local-xpath">
    <xsl:for-each select="ancestor-or-self::node()[@jf:binding]">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="@jf:binding" />
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="build-parent-xpath">
    <xsl:for-each select="ancestor::node()[@jf:binding]">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="@jf:binding" />
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="get-bind-id">
    <xsl:text>bind-</xsl:text>
    <xsl:choose>
      <xsl:when test="@xd:CtrlId">
        <xsl:value-of select="@xd:CtrlId" />
      </xsl:when>
      <xsl:when test="../@xd:CtrlId">
        <xsl:value-of select="../@xd:CtrlId" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>error</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="do-bind-label">
    <xsl:param name="label" />
    <xsl:attribute name="bind">
      <xsl:call-template name="get-bind-id" />
    </xsl:attribute>
    <xsl:if test="$label != ''">
      <xforms:label>
        <xsl:value-of select="$label" />
      </xforms:label>
    </xsl:if>
  </xsl:template>
  <xsl:template name="add-label">
    <xsl:param name="label" />
    <xsl:if test="$label != ''">
      <xforms:label>
        <xsl:value-of select="$label" />
      </xforms:label>
    </xsl:if>
  </xsl:template>

  <!-- Binding templates -->
  <xsl:template match="node()" mode="bind">
    <xsl:apply-templates mode="bind" />
  </xsl:template>
  <xsl:template match="node()[@jf:binding]" mode="bind">
    <xsl:variable name="id">
      <xsl:call-template name="get-bind-id" />
    </xsl:variable>
    <xforms:bind id="{$id}" nodeset="{@jf:binding}">
      <!-- FIXME deal with required -->
      <xsl:attribute name="type">
        <xsl:choose>
          <xsl:when test="contains(@xd:datafmt, 'date')">xs:date</xsl:when>
          <!-- FIXME improve, because number is wider than integer -->
          <xsl:when test="contains(@xd:datafmt, 'number')">xs:integer</xsl:when>
          <xsl:otherwise>xs:string</xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </xforms:bind>
  </xsl:template>
  <xsl:template match="node()[@jf:type='section' or @xd:xctname='RepeatingTable']" mode="bind">
    <xsl:variable name="id">
      <xsl:call-template name="get-bind-id" />
    </xsl:variable>
    <xsl:variable name="nodeset">
      <xsl:if test="not(ancestor::node()[@jf:binding])">
        <xsl:text>instance('form-data')/</xsl:text>
      </xsl:if>
      <xsl:value-of select="ancestor-or-self::node()[@jf:binding][position()=1]/@jf:binding" />
    </xsl:variable>
    <xforms:bind id="{$id}" nodeset="{$nodeset}">
      <xsl:apply-templates mode="bind" />
    </xforms:bind>
  </xsl:template>

  <!-- Trigger events for repeating blocks -->
  <xsl:template name="add-triggers">
    <xsl:param name="id" />
    <xsl:variable name="bind">
      <xsl:call-template name="get-bind-id" />
    </xsl:variable>
    <xforms:trigger>
      <xforms:label>Insert after</xforms:label>
      <xforms:action ev:event="DOMActivate">
        <xforms:insert bind="{$bind}">
          <xsl:attribute name="context">
            <xsl:text>instance('form-data')</xsl:text>
            <xsl:call-template name="build-parent-xpath" />
          </xsl:attribute>
          <xsl:attribute name="origin">
            <xsl:text>instance('template-data')</xsl:text>
            <xsl:call-template name="build-local-xpath" />
          </xsl:attribute>
        </xforms:insert>
      </xforms:action>
    </xforms:trigger>
    <xforms:trigger>
      <xforms:label>Delete</xforms:label>
      <xforms:action ev:event="DOMActivate">
        <xforms:delete at="index('{$id}')" bind="{$bind}" />
      </xforms:action>
    </xforms:trigger>
  </xsl:template>

  <!-- Process HTML -->
  <xsl:template match="html">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <xsl:copy-of select="@*" />
      <xsl:apply-templates />
    </html>
  </xsl:template>

  <!-- Process Head -->
  <xsl:template match="head">
    <xsl:comment>
      <xsl:text disable-output-escaping="yes">&#32;Generated by&#32;</xsl:text>
      <xsl:value-of select="$generator_name" />
      <xsl:text disable-output-escaping="yes">&#32;</xsl:text>
    </xsl:comment>
    <head>
      <!--  TODO i18n -->
      <title>jinFORM - Form Filling</title>
      <xforms:model>
        <xforms:submission id="saveData" ref="instance('form-data')" action="/jinform/saveData" method="post" replace="all" />
        <xforms:instance id="form-data">
          <form-data xmlns="">
            <xsl:copy-of select="document('form-data-instance')" />
          </form-data>
        </xforms:instance>
        <xforms:instance id="template-data">
          <template-data xmlns="">
            <xsl:copy-of select="document('template-data-instance')" />
          </template-data>
        </xforms:instance>
        <xsl:apply-templates select="//body/*" mode="bind" />
      </xforms:model>
    </head>
  </xsl:template>

  <!-- Process Body -->
  <xsl:template match="body">
    <body>
      <xsl:copy-of select="@*[local-name(.)!='binding']" />
      <xforms:group appearance="compact">
        <!--  TODO i18n -->
        <xforms:label>Form Filling</xforms:label>
        <xforms:group appearance="compact">
          <!--  TODO i18n -->
          <xforms:label>Form Actions</xforms:label>
          <a href="{$server_root}">
            <img border="0" src="{$server_image_url}/jinform.png" alt="jinFORM" />
          </a>
          <br />
          <br />
          <xforms:submit submission="saveData">
            <!--  TODO i18n -->
            <xforms:label>Save Data</xforms:label>
          </xforms:submit>
        </xforms:group>
        <xforms:group appearance="compact">
          <xforms:label>
            <xsl:value-of select="$form_name" />
          </xforms:label>
          <xsl:apply-templates />
        </xforms:group>
      </xforms:group>
    </body>
  </xsl:template>

  <!-- Process Repeating Table -->
  <xsl:template match="table[starts-with(@class, 'xdRepeatingTable')]">
    <xsl:apply-templates select="tbody[@xd:xctname='RepeatingTable' and position()=2]" mode="datatable" />
  </xsl:template>
  <xsl:template match="tbody" mode="datatable">
    <xsl:variable name="id" select="generate-id(.)" />
    <p>
      <xforms:repeat id="{$id}" appearance="compact">
        <xsl:attribute name="bind">
          <xsl:call-template name="get-bind-id" />
        </xsl:attribute>
        <p>
          <xsl:for-each select="tr/td">
            <xsl:variable name="index" select="position()" />
            <xsl:apply-templates select=".//span[@jf:binding] | .//select[@jf:binding]">
              <xsl:with-param name="label" select="normalize-space(../../../tbody[@class='xdTableHeader']/tr/td[position()=$index])" />
            </xsl:apply-templates>
          </xsl:for-each>
        </p>
      </xforms:repeat>
      <xsl:call-template name="add-triggers">
        <xsl:with-param name="id" select="$id" />
      </xsl:call-template>
    </p>
  </xsl:template>

  <!-- Remove optional and useless elements -->
  <xsl:template match="node()[@class='optionalPlaceholder'] | @class | @xd:*" />

  <!-- Ignore calendar buttons -->
  <xsl:template match="button[@xd:xctname='DTPicker_DTButton']" />

  <!-- Ignore all spans: specific ones are matched after -->
  <xsl:template match="span" />

  <!-- Ignore all div without style attribute: specific ones are matched after -->
  <xsl:template match="div[not(@style)]">
    <xsl:apply-templates />
  </xsl:template>

  <!-- Non-repeating sections -->
  <xsl:template match="div[@jf:type='section']">
    <xsl:variable name="id" select="generate-id(.)" />
    <p>
      <xforms:group appearance="compact" id="{$id}">
        <xsl:call-template name="do-bind-label" />
        <xsl:apply-templates />
      </xforms:group>
    </p>
  </xsl:template>

  <!-- Repeating sections -->
  <xsl:template match="div[@jf:type='section' and starts-with(@class, 'xdRepeatingSection')]">
    <xsl:variable name="id" select="generate-id(.)" />
    <p>
      <xforms:repeat id="{$id}" appearance="compact">
        <xsl:call-template name="do-bind-label" />
        <p>
          <xsl:apply-templates />
        </p>
      </xforms:repeat>
      <xsl:call-template name="add-triggers">
        <xsl:with-param name="id" select="$id" />
      </xsl:call-template>
    </p>
  </xsl:template>

  <!-- Input -->
  <xsl:template match="span[@xd:xctname='PlainText' or @xd:xctname='DTPicker_DTText']">
    <xsl:param name="label" />
    <xforms:input ref="{@jf:binding}">
      <xsl:call-template name="add-label">
        <xsl:with-param name="label" select="$label" />
      </xsl:call-template>
    </xforms:input>
  </xsl:template>

  <!-- TextArea -->
  <xsl:template match="span[@xd:xctname='PlainText' and contains(@xd:datafmt, 'plainMultiline')]">
    <xsl:param name="label" />
    <xforms:textarea ref="{@jf:binding}">
      <xsl:call-template name="add-label">
        <xsl:with-param name="label" select="$label" />
      </xsl:call-template>
    </xforms:textarea>
  </xsl:template>

  <!-- Select -->
  <xsl:template match="select">
    <xsl:param name="label" />
    <xforms:select1 appearance="minimal" ref="{@jf:binding}">
      <xsl:call-template name="add-label">
        <xsl:with-param name="label" select="$label" />
      </xsl:call-template>
      <xforms:choices>
        <xsl:for-each select="option">
          <xforms:item>
            <xforms:label>
              <xsl:value-of select="normalize-space(text())" />
            </xforms:label>
            <xforms:value>
              <xsl:value-of select="@value" />
            </xforms:value>
          </xforms:item>
        </xsl:for-each>
      </xforms:choices>
    </xforms:select1>
  </xsl:template>

  <!-- Radio button group -->
  <xsl:template match="div[/div/input[@type='radio']]">
    <xsl:param name="label" />
    <xforms:select1 appearance="full" ref="{@jf:binding}">
      <xsl:call-template name="add-label">
        <xsl:with-param name="label" select="$label" />
      </xsl:call-template>
      <xforms:choices>
        <xsl:for-each select="/div/input[@type='radio']">
          <xforms:item>
            <xforms:label>
              <xsl:value-of select="normalize-space(../text())" />
            </xforms:label>
            <xforms:value>
              <xsl:value-of select="@xd:onValue" />
            </xforms:value>
          </xforms:item>
        </xsl:for-each>
      </xforms:choices>
    </xforms:select1>
  </xsl:template>

  <!-- TODO check box, list box -->

  <!--  Redirects Image Source to jinFORM -->
  <xsl:template match="img/@src">
    <xsl:attribute name="src">
      <xsl:choose>
        <xsl:when test="starts-with(., $infopath_resource_prefix)">
          <xsl:value-of select="$server_image_resource_url" />
          <xsl:text>/</xsl:text>
          <xsl:value-of select="substring-after(., $infopath_resource_prefix)" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$server_resource_url" />
          <xsl:text>?src=</xsl:text>
          <xsl:value-of select="." />
          <xsl:text>|</xsl:text>
          <xsl:value-of select="$form_name" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>
