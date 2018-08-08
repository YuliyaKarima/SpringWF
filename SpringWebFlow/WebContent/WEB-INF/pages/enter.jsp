<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div>
		<h2>Sign in to there</h2>

		<form method="post" action="${pageContext.request.contextPath}/login">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<fieldset>
				<table cellspacing="0">
					<tr>
						<th><label for="username_or_email">Username or Email</label></th>
						<td><input id="username_or_email" name="j_username"
							type="text" /></td>
					</tr>
					<tr>
						<th><label for="password">Password</label></th>
						<td><input id="password" name="j_password" type="password" />
							<small><a href="/account/resend_password">Forgot?</a></small></td>
					</tr>
					<tr>
						<th></th>
						<td><input id="remember_me"
							name="_spring_security_remember_me" type="checkbox" /> <label
							for="remember_me" class="inline">Remember me</label></td>
					</tr>
					<tr>
						<th></th>
						<td><input name="commit" type="submit" value="Sign In" /></td>
					</tr>
				</table>
			</fieldset>
		</form>
		<script type="text/javascript">
			document.getElementById('username_or_email').focus();
		</script>
	</div>

</body>
</html>