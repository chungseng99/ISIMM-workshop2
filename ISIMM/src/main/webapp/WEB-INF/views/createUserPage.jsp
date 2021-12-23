<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Create User</title>
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

</head>
<body>

	<%--header start --%>
	<header class="header black-bg">
		<a class="logo"><b>Admin<span>&nbsp;Dashboard</span></b></a>
		<div class="top-menu">
			<nav class="navbar">
				<ul class="nav top-menu ml-auto">
					<li class="nav-item mt-2"><a class="logout" data-toggle="modal"
						href="#logoutModal">Logout</a></li>
				</ul>
			</nav>
		</div>

	</header>
	<%--header end --%>
	<%--side bar start --%>
	<aside>
		<div id="sidebar" >
			<!-- sidebar menu start-->
			<ul class="sidebar-menu" id="nav-accordion">
				<li class="mt"><a href="${pageContext.request.contextPath}/adminDashboard"> <i
						class="fa fa-dashboard"></i> <span>Home</span>
				</a></li>
				<li class="mt"><a class="active" href="redirect:/"> <i
						class="fa fa-dashboard"></i> <span>Create User</span>
				</a></li>
				<li class="mt"><a href="${pageContext.request.contextPath}/viewAll"> <i
						class="fa fa-dashboard"></i> <span>View Users</span>
				</a></li>
				<li class="mt"><a href="${pageContext.request.contextPath}/searchUser"> <i
						class="fa fa-dashboard"></i> <span>Search Users</span>
				</a></li>
			</ul>
			<%--sidebar menu end --%>
		</div>
	</aside>
	<%--side bar end --%>
	<section id="main-content">
		<section class="wrapper">
			<div class="container pt-3 ">
				<h3>
					<i class="fa fa-angle-right"> &nbsp;Create user</i>
				</h3>
				<!-- BASIC FORM ELELEMNTS -->
				<div class="row mt">
					<div class="col-lg-12">
						<div class="form-panel">
							<form class="form-horizontal style-form needs-validation"
								action="createUserForm" method="post" novalidate="novalidate">
								<h4 style="color:red">${message}</h4>
								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Name</label>
									<div class="col-sm-10">
										<input type="text" name="name" class="form-control"
											placeholder="Enter name" required="required">
										<div class="invalid-feedback">Please fill out this
											field.</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">IC.
										number</label>
									<div class="col-sm-10">
										<input type="text" name="icNumber" class="form-control"
											placeholder="Enter IC number without dash"
											pattern="[0-9]{12}" required="required">
										<div class="invalid-feedback">Please enter a valid IC number.</div>
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
									<label for="Role" class="col-sm-2 col-sm-2 control-label">Role</label>
									<div class="radio col-sm-10">
										<label> <input type="radio" name="role" id="Clerk"
											value="CLERK" required="required"> Clerk
										</label>
										
									</div>
									<div class="radio col-sm-10">
										<label> <input type="radio" name="role" id="Teacher"
											value="TEACHER" required="required"> Teacher
										</label>
									</div>
									<div class="radio col-sm-10">
										<label> <input type="radio" name="role" id="Parent"
											value="PARENT" required="required"> Parent

										</label>

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


								<div class="text-center">
									<input type="submit" class="btn btn-primary btn-lg mr-3" name="submit" value="SUBMIT">
									<input type="reset" class="btn btn-danger" name="reset" value="RESET">
								</div>


							</form>
								<div class="text-center">
					<button class="btn btn-danger mt-2" name="cancel" onclick="history.back()"> CANCEL
							</button></div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</section>
	
	<!-- The Modal -->
  <div class="modal fade" id="logoutModal">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Logout Confirmation</h4>
          <button type="button" class="close" data-dismiss="modal">×</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          Are you sure you want to logout?
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        <a href="${pageContext.request.contextPath}/logout"><button type="button" class="btn btn-primary" >YES</button></a>
          <button type="button" class="btn btn-danger" data-dismiss="modal">CANCEL</button>
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