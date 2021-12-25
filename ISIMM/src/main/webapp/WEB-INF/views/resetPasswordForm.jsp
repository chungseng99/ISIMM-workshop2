<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

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

<title>Reset Password</title>
</head>
<body>
<div class="form-group container mt-5">
			<div class="row d-flex justify-content-center">
				<div class="card px-5 py-5">
	<form name='f' action="resetPassword" method='get'
		class="needs-validation" novalidate>
		
					<h2 class="form-login-heading">Reset Password</h2>
					<br> <label for="username">Username:</label> <input
						type='text' class="form-control" name='username'
						required="required" autofocus="autofocus"
						placeholder="Enter username">

					<div class="invalid-feedback">Please enter a valid email.</div>
					<br> <label for="icNumber">IC Number:</label> <input
						type='text' class="form-control" name='icNumber'pattern="[0-9]{12}"
						required="required" placeholder="Enter IC number">

					<div class="invalid-feedback">Please enter a valid IC number.</div>
					<br>
					<p style="color: red">${message}</p>
					<div class="text-center">
						<input class="btn btn-primary" name="submit" type="submit"
							value="SUBMIT" />
							
					</div>
					</form>
				<div class="text-center">
					<a href="${pageContext.request.contextPath}"><button class="btn btn-danger mt-3" name="cancel"
						>CANCEL</button></a>
				</div>
			</div>
			</div>
		</div>
	
	
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