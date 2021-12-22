<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Registration</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/URL/css/style.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/URL/css/style-responsive.css">
<style>
body {
    background-image: url("${pageContext.request.contextPath}/URL/images/school background.png");
    background-size: cover;
    background-position: center center;
    
}
</style>
</head>
<body>
	

		<div class="container pt-3 ">
			<h3>
				<i class="fa fa-angle-right"> &nbsp;Student Registration</i>
			</h3>
			<!-- BASIC FORM ELELEMNTS -->
			<div class="row mt">
				<div class="col-lg-12">
					<div class="form-panel">
						<form class="form-horizontal style-form needs-validation"
							action="studentRegistration" method="post"
							novalidate="novalidate">
							<h4 style="color: red">${message}</h4>
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">Name</label>
								<div class="col-sm-10">
									<input type="text" name="name" class="form-control"
										placeholder="Enter name" required="required">
									<div class="invalid-feedback">Please enter name.</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">IC.
									number</label>
								<div class="col-sm-10">
									<input type="text" name="icNumber" class="form-control"
										placeholder="Enter IC number without hyphen('-' )" pattern="[0-9]{12}"
										required="required">
									<div class="invalid-feedback">Please enter a valid IC
										number</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label" for="email">Email</label>
								<div class="col-sm-10">
									<input type="email" name="email" class="form-control"
										name="email" placeholder="Enter email"
										pattern="[A-Za-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
										required="required">
									<div class="invalid-feedback">Please enter a valid email.</div>
									

								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label" for="email">Username</label>
								<div class="col-sm-10">
									<input type="email" class="form-control" name="username"
										placeholder="Enter Username"
										pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
										required="required" readonly="readonly">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">Phone
									Number</label>
								<div class="col-sm-10">
									<input type="text" name="phoneNumber" class="form-control"
										placeholder="Enter phone number without dash Eg. 0113456754"
										pattern="[0-9]{10,11}">
										<div class="invalid-feedback">Please enter a valid phone number.</div>

								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">Nationality</label>
								<div class="col-sm-10">
									<input type="text" name="nationality" class="form-control"
										placeholder="Enter nationality Eg.Malaysian"
										required="required">
									<div class="invalid-feedback">Please fill out this field.</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">Ethnicity</label>
								<div class="col-sm-10">
									<input type="text" name="ethnicity" class="form-control"
										placeholder="Enter ethnicity Eg.Chinese,Malay,Indian"
										required="required">
									<div class="invalid-feedback">Please fill out this field.</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">Address</label>
								<div class="col-sm-10">
									<textarea name="address" class="form-control" rows="3"></textarea>

								</div>
							</div>
							
							<div class="text-center">
									<input type="submit" class="btn btn-primary btn-lg" name="submit" value="SUBMIT">
									<input type="reset" class="btn btn-danger ml-3" name="reset" value="RESET">
								</div>
						</form>
						<div class="text-center">
						<button class="btn btn-danger mt-3" name="cancel" onclick="history.back()"> CANCEL
							</button></div>
					</div>
				</div>
			</div>
		</div>
	
<script type="text/javascript">
		var firstInput = document.getElementsByName("email")[0];
		var secondInput = document.getElementsByName("username")[0];

		function process(e) {
			secondInput.value = e.target.value.replace(/\s/g);
		}
		firstInput.addEventListener("input", process);

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