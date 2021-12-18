<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Search Result</title>

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
		<div id="sidebar" class="nav-collapse ">
			<!-- sidebar menu start-->
			<ul class="sidebar-menu active" id="nav-accordion">
				<li class="mt "><a href="redirect:/"> <i
						class="fa fa-dashboard"></i> <span>Dashboard</span>
				</a></li>
				<li class="mt"><a href="createUser"> <i
						class="fa fa-dashboard"></i> <span>Create User</span>
				</a></li>
				<li class="mt"><a href="viewAll"> <i
						class="fa fa-dashboard"></i> <span>View Users</span>
				</a></li>
				<li class="mt"><a href="searchUser"> <i
						class="fa fa-dashboard"></i> <span>Search Users</span>
				</a></li>
			</ul>
			<%--sidebar menu end --%>
		</div>
	</aside>
	<%--side bar end --%>
	<section id="main-content">
		<section class="wrapper site-min-height">

			<div class="row mt">
				<div class="col-lg-12">
					<h1>${Message}</h1>
					
				<button class="btn btn-primary" onClick="history.back()">Back</button>
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
</body>
</html>
