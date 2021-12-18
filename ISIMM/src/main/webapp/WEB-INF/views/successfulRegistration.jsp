<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Registration Success</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<style>
body {
	background-image:
		url("${pageContext.request.contextPath}/URL/images/school background.png");
	background-size: cover;
	background-position: center;
}
</style>

</head>
<body>
	<div class="container mt-5 text-center bg-secondary text-dark">
		<h1>User successfully created.</h1>
		<h2>
			Activation link has been sent to ${email}. <br>Please check your
			inbox. Click <a href="${pageContext.request.contextPath}">here</a> to
			sign in
		</h2>
	</div>
</body>
</html>