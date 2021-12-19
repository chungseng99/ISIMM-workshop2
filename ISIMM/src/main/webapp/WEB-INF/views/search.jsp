<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Search User</title>
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
		<div id="sidebar">
			<!-- sidebar menu start-->
			<ul class="sidebar-menu" id="nav-accordion">
				<li class="mt"><a href="adminDashboard"> <i
						class="fa fa-dashboard"></i> <span>Home</span>
				</a></li>
				<li class="mt"><a href="createUser"> <i
						class="fa fa-dashboard"></i> <span>Create User</span>
				</a></li>
				<li class="mt"><a href="viewAll"> <i
						class="fa fa-dashboard"></i> <span>View Users</span>
				</a></li>
				<li class="mt"><a class="active" href="searchUser"> <i
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
					<i class="fa fa-angle-right"> &nbsp;Search User</i>
				</h3>
				<!-- BASIC FORM ELELEMNTS -->
				<div class="row mt">
					<div class="col-lg-12">
						<div class="form-panel">
							<form class="form-horizontal style-form" action="searchUsername"
								method="get" >

								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Search
										By Username</label>
									<div class="col-sm-10">
										<input type="text" name="searchUsername" class="form-control"
											placeholder="Search username" />
											<div class="invalid-feedback">Enter username to search</div>
									</div>
									<br>
									<div class="text-center">
										<input  type="submit" value="Search"
											class="btn btn-primary btn-lg" name="submit">
									</div>

								</div>

							</form>
						</div>
					</div>
				</div>
				<div class="row mt">
					<div class="col-lg-12">
						<div class="form-panel">
							<form class="form-horizontal style-form" action="searchName"
								method="get">

								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Search
										By Name</label>
									<div class="col-sm-10">
										<input type="text" name="searchName" class="form-control"
											placeholder="Search Name" />

									</div>
									<br>
									<div class="text-center">
										<input type="submit" value="Search"
											class="btn btn-primary btn-lg" name="submit">
									</div>
								</div>

							</form>
						</div>
					</div>
				</div>
				<div class="row mt">
					<div class="col-lg-12">
						<div class="form-panel">
							<form class="form-horizontal style-form" action="searchIC"
								method="get">

								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Search
										By IC Number</label>
									<div class="col-sm-10">
										<input type="text" name="searchIC" class="form-control"
											placeholder="Search IC number" />

									</div>
									<br>
									<div class="text-center">
										<input type="submit" value="Search"
											class="btn btn-primary btn-lg" name="submit">
									</div>
								</div>
								
							</form>
						</div>
					</div>
				</div>
				<div class="row mt">
					<div class="col-lg-12">
						<div class="form-panel">
							<form class="form-horizontal style-form" action="searchRole"
								method="get">

								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Search
										By Role</label>
									<div class="col-sm-10">
										<input type="text" name="searchRole" class="form-control"
											placeholder="Search role" />

									</div>
									<br>
									<div class="text-center">
										<input type="submit" value="Search"
											class="btn btn-primary btn-lg" name="submit">
									</div>
								</div>
								
							</form>
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
    