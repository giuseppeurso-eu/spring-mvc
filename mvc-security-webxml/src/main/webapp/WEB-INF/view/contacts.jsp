<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring MVC Webapp</title>
</head>
<body>
<p><a href="/mvc-securityxml/">[Home]</a></p>
<h2>Contacts</h2>

<div>
<table border="1">
	<tr>
		<td><b>Name</b></td>
		<td><b>Email</b></td>
		<td><b>Telephone</b></td>
	</tr>
	<tr>
		<td>${name}</td>
		<td> ${email}</td>
		<td>${tel}</td>
	</tr>
</table>
</div>

<p>
<a href="/mvc-securityxml/logout">>>> LOGOUT </a>
</p>
</body>
</html>