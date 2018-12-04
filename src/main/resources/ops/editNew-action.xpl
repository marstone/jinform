<p:config xmlns:p="http://www.orbeon.com/oxf/pipeline"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:oxf="http://www.orbeon.com/oxf/processors">

    <p:param name="data" type="input"/>
    <p:param name="instance" type="input"/>
    <p:param name="data" type="output"/>

    <p:processor name="oxf:xslt">
        <p:input name="data" href="#instance"/>
        <p:input name="config">
            <config xsl:version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                <url><xsl:value-of select="//selectedFormUrl"/></url>
                <content-type>application/xml</content-type>
                <force-content-type>true</force-content-type>
                <cache-control>
                    <use-local-cache>false</use-local-cache>
                </cache-control>
            </config>
        </p:input>
        <p:output name="data" id="url-config"/>
    </p:processor>
    <p:processor name="oxf:url-generator">
        <p:input name="config" href="#url-config"/>
        <p:output name="data" ref="data"/>
    </p:processor>

</p:config>
