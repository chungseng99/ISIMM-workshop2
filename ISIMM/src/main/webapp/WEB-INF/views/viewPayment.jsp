<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>View Payment</title>

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
						<li class="active"><a
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
						<li><a href="${pageContext.request.contextPath}/studentPage">Student
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchStudent">Search
								Student</a></li>


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
			</ul>
		</div>
	</aside>

	<section id="main-content">
		<section class="wrapper">
			<div class="container pt-3 ">
				<h3>
					<i class="fa fa-angle-right"> &nbsp;View Payment</i>
				</h3>
				<!-- BASIC FORM ELELEMNTS -->
				<div class="row mt">
					<div class="col-lg-12">
						<div class="form-panel">
							<form:form class="form-horizontal style-form needs-validation"
								modelAttribute="payment" method="post" novalidate="novalidate">
								<form:hidden path="paymentId" />
								<div align="center">
								<div class="card" style="width:600px; height:650px">
									<img 
										src="getPaymentProof/<c:out value='${payment.paymentId}'/>"></div>

								</div>
								<br>
								<br>
								<br>
								<div class="form-group">
									<label class="col-sm-2 col-sm-2 control-label">User Id</label>
									<div class="col-sm-10">
										<form:input path="userId" class="form-control" readonly="true" />
									</div>
									<br> <label class="col-sm-2 col-sm-2 control-label">Name</label>
									<div class="col-sm-10">
										<form:input path="name" class="form-control" readonly="true" />
										<br>
									</div>
									<label class="col-sm-2 col-sm-2 control-label">Fee Id</label>
									<div class="col-sm-10">
										<form:input path="feeId" class="form-control" readonly="true" />
										<br>
									</div>
									<label class="col-sm-2 col-sm-2 control-label">Amount</label>
									<div class="col-sm-10">
										<form:input path="paymentAmount" class="form-control"
											readonly="true" />
										<br>
									</div>
									<label class="col-sm-2 col-sm-2 control-label">Date</label>
									<div class="col-sm-10">
										<form:input path="paymentDate" class="form-control"
											readonly="true" />
										<br>
									</div>
									<label class="col-sm-2 col-sm-2 control-label">Status</label>
									<div class="col-sm-10">
										<form:input path="status" class="form-control" readonly="true"/>

									</div>
								</div>
								
								<div class="text-center">
								<a class="mr-3" href="${pageContext.request.contextPath}/approvePayment?paymentId=${payment.paymentId }"><input
										type="button" class="btn btn-primary btn-lg" value="Approve"></a>
										<a href="${pageContext.request.contextPath}/rejectPayment?paymentId=${payment.paymentId }"><input
										type="button" class="btn btn-danger" value="Reject"></a>
										</div>
										<div class="text-center">
										<input type="button"
										class="btn btn-danger mt-2" name="cancel" value="CANCEL" onClick="history.back()">	
										</div>

							</form:form>
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