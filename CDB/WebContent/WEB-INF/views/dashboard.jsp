<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/css/font-awesome.min.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/css/main.css"/>" rel="stylesheet" media="screen">
<script src="<c:url value="/js/jquery.min.js"/>" ></script>
<script src="<c:url value="/js/bootstrap.min.js"/>" ></script>
<script src="<c:url value="/js/dashboard.js"/>" ></script>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a id="home-button" class="navbar-brand" href="dashboard"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${totalComputers} Computers found</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="#" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<form name="sortingForm" action="#" method="POST">
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<!-- Variable declarations for passing labels as parameters -->
							<!-- Table header for Computer Name -->
	
							<th class="editMode" style="width: 60px; height: 22px;"><input
								type="checkbox" id="selectall" /> <span
								style="vertical-align: top;"> - <a href="#"
									id="deleteSelected" onclick="$.fn.deleteSelected();"> 
									<i class="fa fa-trash-o fa-lg"></i>
								</a>
							</span></th>
								<th>Computer name  <button name="sortCriteria" type="submit" class="fa fa-sort-asc" value="computerName;desc"></button>
									<button name="sortCriteria" type="submit" class="fa fa-sort-desc" value="computerName;asc"></button>
								</th>
								<th>Introduced date  <button name="sortCriteria" type="submit" class="fa fa-sort-asc" value="introduced;desc"></button>
									<button name="sortCriteria" type="submit" class="fa fa-sort-desc" value="introduced;asc"></button>
								</th>
								<!-- Table header for Discontinued Date -->
								<th>Discontinued date <button name="sortCriteria" type="submit" class="fa fa-sort-asc" value="discontinued;desc"></button>
									<button name="sortCriteria" type="submit" class="fa fa-sort-desc" value="discontinued;asc"></button>
								</th>
								<!-- Table header for Company -->
								<th>Company  <button name="sortCriteria" type="submit" class="fa fa-sort-asc" value="companyName;desc"></button>
									<button name="sortCriteria" type="submit" class="fa fa-sort-desc" value="companyName;asc"></button>
								</th>
						</tr>
					</thead>
					<!-- Browse attribute computers -->
					<tbody id="results">
						<c:forEach var="computer" items="${computers}">
							<tr>
								<td class="editMode"><input type="checkbox" name="cb" class="cb" value="${computer.id}"></td>
								<td><a href="editComputer?id=${computer.id}" >${computer.name}</a></td>
								<td>${computer.introduced}</td>
								<td>${computer.discontinued}</td>
								<td>${computer.companyName}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<li><a href="#" aria-label="Previous"> <span
						aria-hidden="true">&laquo;</span>
				</a></li>
				<li><a href="dashboard?pageIndex=1">1</a></li>
				<c:choose>	
					<c:when test="${maxPages <= 9}">
					<c:forEach begin="2" end="${maxPages}" varStatus = "loop">
						<li><a href="dashboard?pageIndex=${loop.index}">${loop.index}</a></li>
					</c:forEach>
					</c:when>
					<c:otherwise>
						<li><a>...</a></li>
						<c:choose>
							<c:when test="${maxPages == 10}">
								<c:forEach begin="4" end="${maxPages}" varStatus = "loop">
									<li><a href="dashboard?pageIndex=${loop.index}">${loop.index}</a></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:set var="leftPageButtons" scope="page" value="${Math.min(4, Math.max(pageIndex -2,-1))}"/>
								<c:set var="rightPageButtons" scope="page" value="${Math.min(4,maxPages - pageIndex - 1)}"/>
								<c:forEach begin="${pageIndex - leftPageButtons}" end="${pageIndex+rightPageButtons}" varStatus = "loop">
									<li><a href="dashboard?pageIndex=${loop.index}">${loop.index}</a></li>
								</c:forEach>
							</c:otherwise>
						</c:choose>
						<li><a>...</a></li>
						<li><a href="dashboard?pageIndex=${maxPages}">${maxPages}</a></li>
					</c:otherwise>
				</c:choose>
				<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span></a></li>
			</ul>
				<div class="btn-group btn-group-sm pull-right" role="group">
					<form action="dashboard" method="POST">
						<button name="displayedRowsPerPage" type="submit" class="btn btn-default" value="10">10</button>
						<button name="displayedRowsPerPage" type="submit" class="btn btn-default" value="50">50</button>
						<button name="displayedRowsPerPage" type="submit" class="btn btn-default" value="100">100</button>
					</form>
				</div>

		</div>
	</footer>
</body>
</html>