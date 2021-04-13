<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="/CDB/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="/CDB/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="/CDB/css/main.css" rel="stylesheet" media="screen">
<script src="/CDB/js/jquery.min.js" type="text/javascript"></script>
<script src="webjars/jquery-validation/1.19.3/jquery.validate.min.js" type="text/javascript"></script>
<script src="/CDB/js/addComputerValidation.js" type="text/javascript"></script>
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
        </div>
    </header>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${id} 
                    </div>
                    <h1>Edit Computer</h1>

                    <form:form modelAttribute="computer" id="editComputerForm" action="editComputer" method="POST">
                        <fieldset>
                            <form:input name="id" path="id" type="hidden" id="id"/>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <form:input name="computerName"  path="name" type="text" class="form-control" id="computerName" placeholder="Computer name"/>
                         		<form:errors path="name" cssClass="error"/>    
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <form:input name="introduced" path="introduced" type="date" class="form-control" id="introduced" placeholder="Introduced date"/>
                            	<form:errors path="introduced" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <form:input name="discontinued" path="discontinued" type="date" class="form-control" id="discontinued" placeholder="Discontinued date"/>
                           		<form:errors path="discontinued" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <form:select name="companyId" path="companyId" class="form-control" id="companyId" >
                                  <form:option  value="0">--</form:option>
                                	<c:forEach var="company" items="${companies}">
                                		<form:option value="${company.id}">${company.name}</form:option>
                                	</c:forEach>
                                </form:select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Edit" class="btn btn-primary">
                            or
                            <a href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>