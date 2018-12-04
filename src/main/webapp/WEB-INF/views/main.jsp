<%@ page language="java" session="false" contentType="text/xml; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<fmt:bundle basename="org.jinform.messages" prefix="label.">
<html xmlns="http://www.w3.org/1999/xhtml"
	  xmlns:xforms="http://www.w3.org/2002/xforms"
	  xmlns:xs="http://www.w3.org/2001/XMLSchema"
		xmlns:ev="http://www.w3.org/2001/xml-events"
	  xmlns:widget="http://orbeon.org/oxf/xml/widget">

	<head>
	<title><fmt:message key="jinform" /> - <fmt:message key="main.menu" /></title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />

	<xforms:model>
		<xforms:submission id="editNew" action="/jinform/editNew" method="post" replace="all" />
		<xforms:submission id="uploadData" action="/jinform/uploadData" method="post" replace="all" />
		<xforms:instance>
			<viewData viewName="main" xmlns="">
			<selectedFormUrl />
			<uploadedFormData filename="" mediatype="" />
			</viewData>
		</xforms:instance>
		<xforms:bind id="form" nodeset="/viewData/selectedFormUrl" type="xs:string" />
		<xforms:bind id="data" nodeset="/viewData/uploadedFormData" type="xs:base64Binary">
			<xforms:bind id="data-filename" nodeset="@filename" />
			<xforms:bind id="data-mediatype" nodeset="@mediatype" />
		</xforms:bind>
	</xforms:model>
	</head>

	<body>
	<xforms:group appearance="compact">
		<xforms:label>
			<fmt:message key="main.menu" />
		</xforms:label>
		<img src="<c:out value='${applicationScope.jinformRootUrl}'/>/images/jinform.png" alt="jinFORM" />
		<%--
				List of loaded forms
			--%>
		<xforms:group appearance="compact">
			<xforms:label>
				<fmt:message key="form.manager" />
			</xforms:label>

			<widget:tabs>
				<c:if test="${not empty requestScope.lists.validForms}">
					<widget:tab id="valid-forms">
						<widget:label>
							<fmt:message key="form.valid" />
						</widget:label>

						<xforms:group appearance="compact">
							<xforms:select1 appearance="compact" bind="form">
								<xforms:label>
									<fmt:message key="form.validforms" />
								</xforms:label>
								<xforms:choices>
									<c:forEach items="${requestScope.lists.validForms}" var="formsInfo">
										<xforms:item>
											<xforms:label>
												<c:out value="${formsInfo.fileName}" />
											</xforms:label>
											<xforms:value>
												<c:out value="${requestScope.formRenderingUrl}${formsInfo.formName}" />
											</xforms:value>
										</xforms:item>
									</c:forEach>
								</xforms:choices>
							</xforms:select1>
						</xforms:group>
						<xforms:submit submission="editNew">
							<xforms:label>
								<fmt:message key="form.go" />
							</xforms:label>
						</xforms:submit>
					</widget:tab>
				</c:if>

				<c:if test="${not empty requestScope.lists.invalidForms}">
					<widget:tab id="invalid-forms">
						<widget:label>
							<fmt:message key="form.invalid" />
						</widget:label>
						<c:forEach items="${requestScope.lists.invalidForms}" var="formsInfo">
							<ul>
								<li><c:out value="${formsInfo.fileName}" /></li>
							</ul>
						</c:forEach>
					</widget:tab>
				</c:if>
			</widget:tabs>
		</xforms:group>
		<%--
				Upload exisiting instance
			  TODO reactivate when controller ready
		<xforms:group appearance="compact">
			<xforms:label>
				<fmt:message key="form.resume" />
			</xforms:label>
			<xforms:group appearance="compact">
				<xforms:upload bind="data" mediatype="text/xml">
					<xforms:label>
						<fmt:message key="form.uploadselect" />
					</xforms:label>
					<xforms:filename ref="@filename" />
					<xforms:mediatype ref="@mediatype" />
				</xforms:upload>
			</xforms:group>
			<xforms:output bind="data-filename" />
			<xforms:submit submission="uploadData">
				<xforms:label>
					<fmt:message key="form.upload" />
				</xforms:label>
			</xforms:submit>
		</xforms:group>
	--%>
	</xforms:group>
	</body>
</html>
</fmt:bundle>
