<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="/CDB/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="/CDB/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="/CDB/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
        </div>
    </header>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add Computer</h1>
                    <form id="addComputerForm" name="action="addComputer" method="POST">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input name="computerName" type="text" class="form-control" id="computerName" placeholder="Computer name">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input name="introduced" type="date" class="form-control" id="introduced" placeholder="Introduced date">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input name="discontinued" type="date" class="form-control" id="discontinued" placeholder="Discontinued date">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select name="companyId" class="form-control" id="companyId" >
                                    <option  value="0">--</option>
                                	<c:forEach var="company" items="${companies}">
                                		<option value="${company.id}">${company.name}</option>
                                	</c:forEach>
                                </select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input name="newComputer" type="submit" value="Add" class="btn btn-primary">
                            or
                            <a id="cancelButton" href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>
<script src="webjars/jquery/3.6.0/jquery.min.js" type="text/javascript"></script>
<script src="webjars/jquery-validation/1.19.3/jquery.validate.min.js" type="text/javascript"></script>
<script src="webjars/jquery-validation/1.19.3/additional-methods.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	jQuery.validator.addMethod("greaterThan", 
			function(value, element, params) {
			    if (!/Invalid|NaN/.test(new Date(value)) && !/Invalid|NaN/.test(new Date($(params).val()))){
			        return new Date(value) > new Date($(params).val());
			    }
			    return true; 
			});
	$("#addComputerForm").validate({
	 rules: {
	   computerName: {
		   required: true,
		   nowhitespace: true,
	   },
	   discontinued: { greaterThan: "#introduced" }
	 },
	 messages: {
		 discontinued: "Discontinued date must be after introduced date",
	  },
	});
});
</script>
</html>