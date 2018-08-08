<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/structure.css" type="text/css" />

<head>
<title>Login Page</title>
</head>
<body>
	<c:if test="${'fail' eq param.auth}">
		<div style="color: red">
			Login Failed!!!<br /> Reason :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	<form action="${pageContext.request.contextPath}/login" method="post">
		<fieldset class="box login">
			<table>
				<tr>
					<td>Username:</td>
					<td><input type='text' name='username' /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='password'></td>
				</tr>
				<tr>
					<td colspan='2'><input name="submit" type="submit"
						value="Submit" class="btnLogin"></td>
				</tr>
			</table>
			
		<a href="/login/facebook"> <img src="${pageContext.request.contextPath}/resources/facebook.png" alt="Login with Facebook"></img></a> 
		<a href="/login/github"> <img src="${pageContext.request.contextPath}/resources/github.png"	alt="Login with Github"></img></a> 
		<a href="/login/vk"> <img src="${pageContext.request.contextPath}/resources/vk.png" alt="Login with VK"></img></a> 
		<a href="/login/google"> <img src="${pageContext.request.contextPath}/resources/google.png" alt="Login with Google"></img></a> 
		<a href="${authUrl}"> <img src="${pageContext.request.contextPath}/resources/twitter.png" alt="Login with Twitter"></img></a>
			
		</fieldset>		
	</form>
</body>
</html>
