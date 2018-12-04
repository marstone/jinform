<p:config xmlns:p="http://www.orbeon.com/oxf/pipeline" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:oxf="http://www.orbeon.com/oxf/processors">

	<p:param name="instance" type="input" />
	<p:param name="data" type="output" />

	<p:processor name="oxf:identity">
		<p:input name="data" href="#instance#xpointer(/form-data/child::node())" />
		<p:output name="data" ref="data" />
	</p:processor>
</p:config>
