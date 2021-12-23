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

<title>Change Password</title>
</head>
<body>
	
		<div class="form-group container mt-5">
			<div class="row d-flex justify-content-center">
				<div class="card px-5 py-5">
				<form name='f' action="changePassword" method='post'
		class="needs-validation" novalidate>
					<h2 class="form-login-heading">Change Password</h2>
					<br> <label for="password">New Password:</label> <input
						type='password' class="form-control" name='password' id='password' minlength="8"
						required="required" autofocus="autofocus" 
						placeholder="Enter new password" >
						<div class="invalid-feedback">Enter password of at least 8 characters </div>

					<br> <label for="confirmPassword">Confirm password:</label> <input
						type='password' class="form-control" name='confirmPassword'
						id='confirmPassword' required="required"
						placeholder="Enter password" minlength="8">
					<div class="invalid-feedback">Enter password of at least 8 characters </div>


					<br>

					<div class="text-center">
						<input class="btn btn-primary" type="submit" name="btnsbumit" value="SUBMIT"
							onClick="myFunction(event)"/>
							
					</div>
					</form>
					<div class="text-center">
					<button class="btn btn-danger mt-3" name="cancel" onclick="history.back()"> CANCEL
							</button></div>
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
	<script type="text/javascript">
		function myFunction(event) {
			var pw1 = document.getElementById("password").value;
			var pw2 = document.getElementById("confirmPassword").value;
			var passLength1=document.getElementById("password").value.length;
			var passLength2=document.getElementById("confirmPassword").value.length;
			if ((pw1 === pw2)&&(passLength1 != 0)&&(passLength2 != 0)&&(passLength1 >= 8)&&(passLength2 >= 8)) {
				
			alert("Passwords successfully changed");
				document.getElementById("myForm").submit();
				return true;
				
		} else if((pw1 === pw2)&&(passLength1 < 8)&&(passLength2 < 8)&&(passLength1 != 0)&&(passLength2 != 0)){
			
			alert("Enter at least 8 characters");
			event.preventDefault();
			event.stopPropagation();
		
		
		}else if((passLength1 == 0)&&(passLength2 == 0)){
		
			alert("Passwords is empty");
			event.preventDefault();
			event.stopPropagation();
		
		
		}else{
				alert("Passwords did not match");
				event.preventDefault();
				event.stopPropagation();
				return false;
			}
		}
	</script>
</body>
</html>