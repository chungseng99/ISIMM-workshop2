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
						<li ><a
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
						<li ><a
							href="${pageContext.request.contextPath}/announcementPage">Announcement
								List</a></li>
						<li><a
							href="${pageContext.request.contextPath}/createAnnouncementForm">Create
								Announcement</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchAnnouncement">Search
								Announcement</a></li>

					</ul></li>

			</ul>
		</div>
	</aside>
	<section id="main-content">
		<section class="wrapper site-min-height">

			<div class="row mt">
				<div class="col-lg-12">

					<a href="${pageContext.request.contextPath}/createAnnouncementForm"><button
							class="btn btn-primary">Create Announcement</button></a> &nbsp;<a
						href="${pageContext.request.contextPath}/searchAnnouncement"><button
							class="btn btn-primary">Search Announcement</button></a><br> <br>

					<h1>Announcement List</h1>
					<c:set value="${announcement}" var="announcementList" />
					<table class="table table-hover table-bordered">
						<thead class="thead-dark">
							<tr>
								<th>No</th>
								<th width="50%">Title</th>
								<th>Date</th>
								<th width="20%">Creator</th>
								<th width="15%">Action</th>
							</tr>
						</thead>

						<c:forEach items="${announcementList.pageList}" var="announcement" varStatus="status">
							<tr>
								<td>${status.index + 1}</td>
								<td>${announcement.title}</td>
								<td>${announcement.dateCreated}</td>
								<td>${announcement.name}</td>
								

								<td><a class="mr-3" href="${pageContext.request.contextPath}/editAnnouncement?announcementId=${announcement.announcementId }"><input
										type="button" class="btn btn-primary" value="Edit/View"></a>
										<div class="mt-2"><a data-toggle="modal" href="#sendModal${announcement.announcementId }"><input 
										type="button" class="btn btn-primary" value="Send"></a></div>
									<div class="mt-2"> <a data-toggle="modal"
									href="#deleteModal${announcement.announcementId }"><input type="button"
										class="btn btn-danger" value="Delete"></a></div></td>

							</tr>
							<!-- The Modal -->
							<div class="modal fade" id="deleteModal${announcement.announcementId }">
								<div class="modal-dialog">
									<div class="modal-content">

										<!-- Modal Header -->
										<div class="modal-header">
											<h4 class="modal-title">Delete Announcement</h4>
											<button type="button" class="close" data-dismiss="modal">×</button>
										</div>

										<!-- Modal body -->
										<div class="modal-body" align="left">Are you sure you
											want to delete the announcement?</div>

										<!-- Modal footer -->
										<div class="modal-footer">
											<a href="${pageContext.request.contextPath}/deleteAnnouncement?announcementId=${announcement.announcementId }"><button
													type="button" class="btn btn-primary">YES</button></a>
											<button type="button" class="btn btn-danger"
												data-dismiss="modal">CANCEL</button>
										</div>

									</div>
								</div>
							</div>
							
							<!-- The Modal -->
							<div class="modal fade" id="sendModal${announcement.announcementId }">
								<div class="modal-dialog">
									<div class="modal-content">

										<!-- Modal Header -->
										<div class="modal-header">
											<h4 class="modal-title">Send Announcement</h4>
											<button type="button" class="close" data-dismiss="modal">×</button>
										</div>

										<!-- Modal body -->
										<div class="modal-body" align="left">Are you sure you
											want to send the announcement to student and parent?</div>

										<!-- Modal footer -->
										<div class="modal-footer">
											<a href="${pageContext.request.contextPath}/sendAnnouncement?announcementId=${announcement.announcementId }"><button
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
								<c:when test="${announcementList.firstPage}">
									<li><span class="page-link">Prev</span></li>
								</c:when>
								<c:otherwise>
									<c:url value="prev" var="url" />
									<li><a class="page-link"
										href='<c:out value="${pageContext.request.contextPath}/searchCreator/${url}?searchCreator=${search}&submit=Search" />'>Prev</a></li>
								</c:otherwise>
							</c:choose>
							<c:forEach begin="1" end="${announcementList.pageCount}" step="1"
								varStatus="tagStatus">
								<c:choose>
									<%-- In PagedListHolder page count starts from 0 --%>
									<c:when test="${(announcementList.page + 1) == tagStatus.index}">
										<li><span class="page-link">${tagStatus.index}</span></li>
									</c:when>
									<c:otherwise>
										<c:url value="${tagStatus.index}" var="url" />
										<li><a class="page-link"
											href='<c:out value="${pageContext.request.contextPath}/searchCreator/${url}?searchCreator=${search}&submit=Search" />'>${tagStatus.index}</a></li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<%-- Show Next as link if not on last page --%>
								<c:when test="${announcementList.lastPage}">
									<li><span class="page-link">Next</span></li>
								</c:when>
								<c:otherwise>
									<c:url value="next" var="url" />
									<li><a class="page-link"
										href='<c:out value="${pageContext.request.contextPath}/searchCreator/${url}?searchCreator=${search}&submit=Search" />'>Next</a></li>
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