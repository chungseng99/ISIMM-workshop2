<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
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
    background-image: url("${pageContext.request.contextPath}/URL/images/school background.png");
    background-size: cover;
    background-position: center center;
    
}
</style>

<title>Login</title>
</head>
<body>



	<!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
	<form name='f'
		action="${pageContext.request.contextPath}/j_spring_security_check"
		method='POST' class="needs-validation" novalidate>
		<div class="form-group container mt-5">
			<div class="row d-flex justify-content-center">
				<div class="card px-5 py-5">
					<h2 class="form-login-heading">Sign In</h2>
					<br> <label for="username">Username:</label> <input
						type='text' class="form-control" name='username'
						required="required" autofocus="autofocus"
						placeholder="Enter email">
					<div class="invalid-feedback">Please fill out this field.</div>
<br>
					<label for="password">Password:</label> <input type='password'
						class="form-control" name='password' required="required"
						placeholder="Enter password" />
					<div class="invalid-feedback">Please fill out this field.</div>
					<br>
					<div class="text-center">
						<input class="btn btn-primary btn-block" name="submit" type="submit"
							value="SIGN IN" />
						<c:if test="${param.error=='true'}">
							<div style="color: red; margin: 10px 0px;">

								Login Failed<br> Invalid credentials

							</div>
						</c:if>
					</div>
					<a 
						href="reset"> Forgot Password?</a>
					
					<div class="registration text-center">
						<hr>
						Student Registration<br /> <a class="" href="studentRegister"> Create
							student account </a>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script>
		// Disable form submissions if there are invalid fields
		(function() {
			'use strict';
			window.addEventListener('load',
					function() {
						// Get the forms we want to add validation styles to
						var forms = document
								.getElementsByClassName('needs-validation');
						// Loop over them and prevent submission
						var validation = Array.prototype.filter.call(forms,
								function(form) {
									form.addEventListener('submit', function(
											event) {
										if (form.checkValidity() === false) {
											event.preventDefault();
											event.stopPropagation();
										}
										form.classList.add('was-validated');
									}, false);
								});
					}, false);
		})();
	</script>

</body>
</html>