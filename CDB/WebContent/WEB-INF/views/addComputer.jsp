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
                    <h1>Add Computer</h1>
                    <form:form modelAttribute="computer" id="addComputerForm" action="addComputer" method="POST">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <form:input path="name" name="computerName" type="text" class="form-control" id="computerName" placeholder="Computer name"/>
                         		<form:errors path="name" cssClass="error"/>        
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <form:input path="introduced" name="introduced" type="date" class="form-control" id="introduced" placeholder="Introduced date"/>
                           		<form:errors path="introduced" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <form:input path="introduced" name="discontinued" type="date" class="form-control" id="discontinued" placeholder="Discontinued date"/>
                           		<form:errors path="introduced" cssClass="error"/>  
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <form:select path="companyId" name="companyId" class="form-control" id="companyId" >
                                    <form:option  value="0">--</form:option>
                                	<c:forEach var="company" items="${companies}">
                                		<form:option value="${company.id}">${company.name}</form:option>
                                	</c:forEach>
                                </form:select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input name="newComputer" type="submit" value="Add" class="btn btn-primary"/>
                            or
                            <a id="cancelButton" href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>