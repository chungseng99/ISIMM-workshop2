<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
				<li class="mt "><a href="${pageContext.request.contextPath}/adminDashboard"> <i
						class="fa fa-dashboard"></i> <span>Home</span>
				</a></li>
				<li class="mt"><a href="${pageContext.request.contextPath}/createUser"> <i
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
		<section class="wrapper site-min-height">

			<div class="row mt">
				<div class="col-lg-12">
					<a href="createUser"><button class="btn btn-primary">Create
							new User</button></a>&nbsp;<a href="searchUser"><button
							class="btn btn-primary">Search Users</button></a><br>
					<br>

					<div align="center">
						<h1>User List</h1>
<c:set value="${userList}" var="userPageList" />
						<table class="table table-hover table-bordered">
							<thead class="thead-dark">
								<tr>
									<th>No</th>
									<th>Username</th>
									<th>Name</th>
									<th>IC Number</th>
									<th>Role</th>
									<th>Permission</th>
									<th>Action</th>
								</tr>
							</thead>

							<c:forEach items="${userPageList.pageList}" var="user" varStatus="status">
								<tr>
									<td>${status.index + 1}</td>
									<td>${user.username}</td>
									<td>${user.name}</td>
									<td>${user.icNumber}</td>
									<td>${user.role}</td>
									<td>${user.enabled}</td>


									<td><a href="${pageContext.request.contextPath}/edit?userId=${user.userId}"><input
											type="button" class="btn btn-primary" value="Edit"></a>
										&nbsp;&nbsp;&nbsp;&nbsp; <a data-toggle="modal"
										href="#deactivateModal${user.userId }"><input type="button"
											class="btn btn-danger" value="Deactivate"></a>
											<div class="mt-2">
											<a data-toggle="modal" 
										href="#deleteModal${user.userId }"><input type="button" <c:if test="${user.enabled==true}"><c:out value="disabled='disabled'"/></c:if>
											class="btn btn-danger" value="Delete"></a></div></td>

								</tr>
								<!-- The Modal -->
								<div class="modal fade" id="deactivateModal${user.userId }">
									<div class="modal-dialog">
										<div class="modal-content">

											<!-- Modal Header -->
											<div class="modal-header">
												<h4 class="modal-title">Deactivate user</h4>
												<button type="button" class="close" data-dismiss="modal">?</button>
											</div>

											<!-- Modal body -->
											<div class="modal-body" align="left">Are you sure you want to
												deactivate the user?</div>

											<!-- Modal footer -->
											<div class="modal-footer">
												<a href="${pageContext.request.contextPath}/deactivate?userId=${user.userId}"><button
														type="button" class="btn btn-primary">YES</button></a>
												<button type="button" class="btn btn-danger"
													data-dismiss="modal">CANCEL</button>
											</div>

										</div>
									</div>
								</div>
								<!-- The Modal -->
								<div class="modal fade" id="deleteModal${user.userId }">
									<div class="modal-dialog">
										<div class="modal-content">

											<!-- Modal Header -->
											<div class="modal-header">
												<h4 class="modal-title">Delete user</h4>
												<button type="button" class="close" data-dismiss="modal">?</button>
											</div>

											<!-- Modal body -->
											<div class="modal-body" align="left">Are you sure you want to
												delete the user?</div>

											<!-- Modal footer -->
											<div class="modal-footer">
												<a href="delete?userId=${user.userId}"><button
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
  <c:when test="${userPageList.firstPage}">
   <li> <span class="page-link">Prev</span></li>
  </c:when>
  <c:otherwise>
    <c:url value="prev" var="url" />                  
    <li><a class="page-link" href='<c:out value="${pageContext.request.contextPath}/searchRole/${url}?searchRole=${search}&submit=Search" />'>Prev</a></li>
  </c:otherwise>
</c:choose>
<c:forEach begin="1" end="${userPageList.pageCount}" step="1"  varStatus="tagStatus">
  <c:choose>
    <%-- In PagedListHolder page count starts from 0 --%>
    <c:when test="${(userPageList.page + 1) == tagStatus.index}">
     <li> <span class="page-link">${tagStatus.index}</span></li>
    </c:when>
    <c:otherwise>
      <c:url value="${tagStatus.index}" var="url" />                  
      <li><a class="page-link" href='<c:out value="${pageContext.request.contextPath}/searchRole/${url}?searchRole=${search}&submit=Search" />'>${tagStatus.index}</a></li>
    </c:otherwise>
  </c:choose>
</c:forEach>
<c:choose>
  <%-- Show Next as link if not on last page --%>
  <c:when test="${userPageList.lastPage}">
   <li> <span class="page-link">Next</span></li>
  </c:when>
  <c:otherwise>
  <c:url value="next" var="url" />                  
    <li><a class="page-link" href='<c:out value="${pageContext.request.contextPath}/searchRole/${url}?searchRole=${search}&submit=Search" />'>Next</a></li>
  </c:otherwise>
</c:choose>
</ul>
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
          <button type="button" class="close" data-dismiss="modal">?</button>
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