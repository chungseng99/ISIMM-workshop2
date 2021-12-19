<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student List</title>

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
							href="${pageContext.request.contextPath}/announcementPage">Announcement
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
						<li><a href="${pageContext.request.contextPath}/feePage">Fee
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/createFeeForm">Create
								Fee</a></li>

						<li><a href="${pageContext.request.contextPath}/searchFee">Search
								Fee</a></li>
					</ul>
				<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Student</span>
				</a>
					<ul class="sub">
						<li class="active"><a
							href="${pageContext.request.contextPath}/studentPage">Student
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchStudent">Search
								Student</a></li>


					</ul>
			</ul>
		</div>
	</aside>
	<section id="main-content">
		<section class="wrapper site-min-height">

			<div class="row mt">
				<div class="col-lg-12">
					<a href="${pageContext.request.contextPath}/searchStudent"><button
							class="btn btn-primary">Search Student</button></a><br> <br>
					<div align="center">
					<h1>Student List</h1></div>
					<c:set value="${student}" var="studentList" />
					<table class="table table-hover table-bordered">
						<thead class="thead-dark">
							<tr>
								<th>No</th>
								<th>Name</th>
								<th>IC Number </th>
								<th>Email</th>
								<th width=10%>Phone Number</th>
								<th width="10%">Action</th>
							</tr>
						</thead>

						<c:forEach items="${studentList.pageList}" var="student"
							varStatus="status">
							<tr>
								<td>${status.index + 1}</td>
								<td>${student.name}</td>
								<td>${student.icNumber}</td>
								<td>${student.email}</td>
								<td>${student.phoneNumber}</td>




								<td><a class="mr-3"
									href="${pageContext.request.contextPath}/viewStudent?userId=${student.userId }"><input
										type="button" class="btn btn-primary" value="View"></a>
							</tr>
						</c:forEach>
					</table>
					<div class='d-flex'>
						<ul class="pagination mx-auto">
							<c:choose>
								<%-- Show Prev as link if not on first page --%>
								<c:when test="${studentList.firstPage}">
									<li><span class="page-link">Prev</span></li>
								</c:when>
								<c:otherwise>
									<c:url value="prev" var="url" />
									<li><a class="page-link"
										href='<c:out value="${pageContext.request.contextPath}/studentPage/${url}" />'>Prev</a></li>
								</c:otherwise>
							</c:choose>
							<c:forEach begin="1" end="${studentList.pageCount}" step="1"
								varStatus="tagStatus">
								<c:choose>
									<%-- In PagedListHolder page count starts from 0 --%>
									<c:when test="${(studentList.page + 1) == tagStatus.index}">
										<li><span class="page-link">${tagStatus.index}</span></li>
									</c:when>
									<c:otherwise>
										<c:url value="${tagStatus.index}" var="url" />
										<li><a class="page-link"
											href='<c:out value="${pageContext.request.contextPath}/studentPage/${url}" />'>${tagStatus.index}</a></li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<%-- Show Next as link if not on last page --%>
								<c:when test="${studentList.lastPage}">
									<li><span class="page-link">Next</span></li>
								</c:when>
								<c:otherwise>
									<c:url value="next" var="url" />
									<li><a class="page-link"
										href='<c:out value="${pageContext.request.contextPath}/studentPage/${url}" />'>Next</a></li>
								</c:otherwise>
							</c:choose>
						</ul>
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