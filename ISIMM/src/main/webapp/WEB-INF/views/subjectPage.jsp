<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Subject List</title>

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
					</ul></li>
				<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Student</span>
				</a>
					<ul class="sub">
						<li><a href="${pageContext.request.contextPath}/studentPage">Student
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchStudent">Search
								Student</a></li>


					</ul></li>
				<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Teacher</span>
				</a>
					<ul class="sub">
						<li><a href="${pageContext.request.contextPath}/teacherPage">Teacher
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchTeacher">Search
								Teacher</a></li>
					</ul>
				<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Payment</span>
				</a>
					<ul class="sub">
						<li><a href="${pageContext.request.contextPath}/paymentPage">Payment
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchPayment">Search
								Payment</a></li>


					</ul>
						<li class="sub-menu"><a href="javascript:;"> <i
						class="fa fa-cogs"></i> <span>Subject</span>
				</a>
					<ul class="sub">
						<li class="active"><a
							href="${pageContext.request.contextPath}/subjectPage">Subject
								List</a></li></ul>
			</ul>
		</div>
	</aside>
	<section id="main-content">
		<section class="wrapper site-min-height">

			<div class="row mt">
				<div class="col-lg-12">

					<a data-toggle="modal" href="#createModal"><button
							class="btn btn-primary">Add Subject</button></a> &nbsp;
					<div align="center">
						<h1>Subject List</h1>
					</div>
					<c:set value="${subject}" var="subjectList" />
					<table class="table table-hover table-bordered">
						<thead class="thead-dark">
							<tr>
								<th>No</th>
								<th>Subject Id</th>
								<th>Subject Name</th>

								<th width="10%">Action</th>
							</tr>
						</thead>

						<c:forEach items="${subjectList.pageList}" var="subject"
							varStatus="status">

							<tr>
								<td>${status.index + 1}</td>
								<td>${subject.subjectId}</td>
								<td>${subject.subjectName}</td>


								<td><a data-toggle="modal"
									href="#deleteModal${subject.subjectId }"><input
										type="button" class="btn btn-danger" value="Delete"></a></td>

							</tr>
							<!-- The Modal -->
							<div class="modal fade" id="deleteModal${subject.subjectId }">
								<div class="modal-dialog">
									<div class="modal-content">

										<!-- Modal Header -->
										<div class="modal-header">
											<h4 class="modal-title">Delete Subject</h4>
											<button type="button" class="close" data-dismiss="modal">�</button>
										</div>

										<!-- Modal body -->
										<div class="modal-body" align="left">Are you sure you
											want to delete the subject?</div>

										<!-- Modal footer -->
										<div class="modal-footer">
											<a
												href="${pageContext.request.contextPath}/deleteSubject?subjectId=${subject.subjectId }"><button
													type="button" class="btn btn-primary">YES</button></a>
											<button type="button" class="btn btn-danger"
												data-dismiss="modal">CANCEL</button>
										</div>

									</div>
								</div>
							</div>
						</c:forEach>
					</table>
					<div class='d-flex'>
						<ul class="pagination mx-auto">
							<c:choose>
								<%-- Show Prev as link if not on first page --%>
								<c:when test="${subjectList.firstPage}">
									<li><span class="page-link">Prev</span></li>
								</c:when>
								<c:otherwise>
									<c:url value="prev" var="url" />
									<li><a class="page-link"
										href='<c:out value="${pageContext.request.contextPath}/subjectPage/${url}" />'>Prev</a></li>
								</c:otherwise>
							</c:choose>
							<c:forEach begin="1" end="${subjectList.pageCount}" step="1"
								varStatus="tagStatus">
								<c:choose>
									<%-- In PagedListHolder page count starts from 0 --%>
									<c:when test="${(subjectList.page + 1) == tagStatus.index}">
										<li><span class="page-link">${tagStatus.index}</span></li>
									</c:when>
									<c:otherwise>
										<c:url value="${tagStatus.index}" var="url" />
										<li><a class="page-link"
											href='<c:out value="${pageContext.request.contextPath}/subjectPage/${url}" />'>${tagStatus.index}</a></li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<%-- Show Next as link if not on last page --%>
								<c:when test="${subjectList.lastPage}">
									<li><span class="page-link">Next</span></li>
								</c:when>
								<c:otherwise>
									<c:url value="next" var="url" />
									<li><a class="page-link"
										href='<c:out value="${pageContext.request.contextPath}/subjectPage/${url}" />'>Next</a></li>
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
					<button type="button" class="close" data-dismiss="modal">�</button>
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

	<!-- The Modal -->
	<div class="modal fade" id="createModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Add Subject</h4>
					<button type="button" class="close" data-dismiss="modal">�</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">
					<form role="form" class="form-horizontal style-form needs-validation"
						action="addSubject" method="post" novalidate="novalidate">
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">Subject Name</label>
							<div class="col-sm-10">
								<input name="subjectName" class="form-control"
									placeholder="Enter subject name" required="required" />
									<div class="invalid-feedback">Enter a subject name</div>
							</div>
							</div>
							<!-- Modal footer -->
				<div class="modal-footer">
					<input type="submit" class="btn btn-primary"
										 value="ADD">
										
					<button type="button" class="btn btn-danger" data-dismiss="modal">CANCEL</button>
				</div>
					</form>
				</div>

				

			</div>
		</div>
	</div>

<script type="text/javascript">
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