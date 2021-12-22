<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Search Announcement</title>

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

<script type="text/javascript" class="include"
	src="${pageContext.request.contextPath}/URL/bootstrap/jquery.dcjqaccordion.2.7.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/URL/bootstrap/common-scripts.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/URL/bootstrap/jquery.nicescroll.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/URL/bootstrap/jquery.scrollTo.min.js"></script>
</head>
<body>
	<%--header start --%>
	<header class="header black-bg">
		<a class="logo"><b>Clerk<span>&nbsp;Dashboard</span></b></a>
		<div class="top-menu">
			<nav class="navbar">
				<ul class="nav top-menu ml-auto">
					<li class="nav-item mt-2"><a class="logout"
						data-toggle="modal" href="#logoutModal">Logout</a></li>
				</ul>
			</nav>
		</div>

	</header>
	<%--header end --%>
	<%--side bar start --%>
	<aside>
		<div id="sidebar" class="nav-collapse ">
			<!-- sidebar menu start-->
			<ul class="sidebar-menu" id="nav-accordion">
				<li class="mt "><a
					href="${pageContext.request.contextPath}/clerkDashboard"> <i
						class="fa fa-dashboard "></i> <span>Home</span>
				</a></li>
				<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Classroom</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/classroomPage">Classroom
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/createClassroomForm">Create
								Classroom</a></li>
						<li ><a
							href="${pageContext.request.contextPath}/searchClassroom">Search
								Classroom</a></li>

					</ul></li>
				<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Announcement</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/announcementPage">Announcement
								List</a></li>
						<li ><a
							href="${pageContext.request.contextPath}/createAnnouncementForm">Create
								Announcement</a></li>
						<li class="active"><a
							href="${pageContext.request.contextPath}/searchAnnouncement">Search
								Announcement</a></li>

					</ul></li>
					<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Fee</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/feePage">Fee
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/createFeeForm">Create Fee</a></li>
							
							<li><a
							href="${pageContext.request.contextPath}/searchFee">Search Fee</a></li>
							</ul>
							
							<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Student</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/studentPage">Student
								List</a></li>
								<li><a
							href="${pageContext.request.contextPath}/searchStudent">Search
								Student</a></li>
								
								
								</ul>
								
								<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Teacher</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/teacherPage">Teacher
								List</a></li>
								<li><a
							href="${pageContext.request.contextPath}/searchTeacher">Search
								Teacher</a></li>
								</ul>
								
								<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Payment</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/paymentPage">Payment
								List</a></li>
								<li><a
							href="${pageContext.request.contextPath}/searchPayment">Search
								Payment</a></li>
								
								
								</ul>
								
									<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Subject</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/subjectPage">Subject
								List</a></li></ul>

			</ul>
		</div>
	</aside>
	
		<section id="main-content">
		<section class="wrapper">
			<div class="container pt-3 ">
				<h3>
					<i class="fa fa-angle-right"> &nbsp;Search Announcement</i>
				</h3>
				<!-- BASIC FORM ELELEMNTS -->
				<div class="row mt">
					<div class="col-lg-12">
						<div class="form-panel">
							<form class="form-horizontal style-form" action="searchTitle"
								method="get" >

								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Search
										By Title</label>
									<div class="col-sm-10">
										<input type="text" name="searchTitle" class="form-control"
											placeholder="Search announcement with title" />
											
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
							<form class="form-horizontal style-form" action="searchCreator"
								method="get" >

								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Search
										By Creator Name</label>
									<div class="col-sm-10">
										<input type="text" name="searchCreator" class="form-control"
											placeholder="Search classrooom with creator name" />
											
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
							<form class="form-horizontal style-form" action="searchDateCreated"
								method="get" >

								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">Search
										By Date Created</label>
									<div class="col-sm-10">
										<input type="text" name="searchDateCreated" class="form-control"
											placeholder="Enter date with format (yyyy-mm-dd or mm-dd or dd or yyyy-mm or mm)"/>
											
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
				
				
				</div>
				</section>
				</section>
				

</body>
</html>