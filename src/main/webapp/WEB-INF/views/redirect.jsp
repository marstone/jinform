<%@ page language="java" session="false" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<fmt:bundle basename="org.jinform.messages" prefix="label.">
<html>
	<head>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<meta http-equiv="refresh" content="0;url=<c:out value='${applicationScope.presentationServerHome}'/>">
	</head>
	<body><p>[<fmt:message key="loading" />...]</p></body>
</html>
</fmt:bundle>