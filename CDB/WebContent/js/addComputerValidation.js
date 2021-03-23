$(function() {
	jQuery.validator.addMethod("greaterThan", 
			function(value, element, params) {
			    if (!/Invalid|NaN/.test(new Date(value)) && !/Invalid|NaN/.test(new Date($(params).val()))){
			        return new Date(value) > new Date($(params).val());
			    }
			    return true; 
			});
	jQuery.validator.addMethod("notBlank", 
			function(value, element, params) {
			    return (value.trim().length != 0) == params;
			}, "A name needs more characters than only spaces");
	$("#addComputerForm").validate({
	 rules: {
	   computerName: {
		   required: true,
		   notBlank: true,
	   },
	   discontinued: { greaterThan: "#introduced" }
	 },
	 messages: {
		 discontinued: "Discontinued date must be after introduced date",
	  },
	});
});