<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>View Attendance</title>

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
						<li><a
							href="${pageContext.request.contextPath}/searchClassroom">Search
								Classroom</a></li>

					</ul></li>
				<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Announcement</span>
				</a>
					<ul class="sub">
						<li><a
							href="${pageContext.request.contextPath}/announcementList">Announcement
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/createAnnouncementForm">Create
								Announcement</a></li>
						<li><a
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
					<i class="fa fa-angle-right"> &nbsp;Attendance</i>
				</h3>
				<br>
				<div class="container bg-dark text-white pt-3 pb-3">
				<h4>
				
      
				Name: ${user.name} <br><br>
				Class: ${className} <br><br>
				Attendance Percentage: <c:choose><c:when test="${percentage=='NaN'}">0.00%</c:when>
				<c:otherwise><fmt:formatNumber type="number" pattern="##.00" value="${percentage}"/>% </c:otherwise></c:choose><br><br>
				Number of days absent: ${absent }<br><br>
				
				<a class="mr-3"
									href="${pageContext.request.contextPath}/printAttendance?userId=${userId }" target="_blank"><input
										type="button" class="btn btn-primary" value="Print Attendance" ></a>
										<input type="button" class="btn btn-danger" name="cancel"
										value="Back" onClick="history.back()">
				</h4></div>
				<!-- BASIC FORM ELELEMNTS -->
				<div class="row mt">
					<div class="col-lg-12">
							<table class="table table-hover table-bordered">
						<thead class="thead-dark">
							<tr>
								<th>No</th>
								<th>Attendance Name</th>
								<th>Attendance Date</th>
								<th>Status</th>
							</tr>
						</thead>

						<c:forEach items="${attendance}" var="attendance" varStatus="status">
							<tr>
								<td>${status.index + 1}</td>
								<td>${attendance.attendanceName}</td>
								<td>${attendance.attendanceDate}</td>
								<td <c:if test="${attendance.status=='absent'}"><c:out value = "style=color:red;"/></c:if>>${attendance.status}</td>
								
										</c:forEach>
										</table>
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
				<div class="modal-body">Are you sure you want to logout?</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<a href="${pageContext.request.contextPath}/logout"><button
							type="button" class="btn btn-primary">YES</button></a>
					<button type="button" class="btn btn-danger" data-dismiss="modal">CANCEL</button>
				</div>

			</div>
		</div>
	</div>
</body>
</html>