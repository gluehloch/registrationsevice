<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.LocalDate"%>
<%@ page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>Registration And Authentication Service</title>
</head>
<body>
	<h1>Registration and Authentication REST Service</h1>
	<ul>
		<li><%=LocalDateTime.now().toString()%></li>
		<li>PathInfo: <%=request.getPathInfo()%></li>
		<li>ServletPath: <%=request.getServletPath()%></li>
		<li>ServerName: <%=request.getServerName()%></li>
		<li>ServletPort: <%=request.getServerPort()%></li>
		<li>ContextPath: <%=request.getContextPath()%></li>
		<li>LocalAddr: <%=request.getLocalAddr()%></li>
		<li>LocalPort: <%=request.getLocalPort()%></li>
		<li>RemoteHost: <%=request.getRemoteHost()%></li>
	</ul>
	<h2>Umlaute:</h2>
	<p>ÄÖÜ äöü ß</p>
</body>
</html>
