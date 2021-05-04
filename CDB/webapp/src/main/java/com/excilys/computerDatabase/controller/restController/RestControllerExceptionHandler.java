package com.excilys.computerDatabase.controller.restController;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.excilys.computerDatabase.exception.CompanyNotFoundException;
import com.excilys.computerDatabase.exception.ComputerNotFoundException;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
	
	  @ExceptionHandler(value = { ComputerNotFoundException.class })
	  protected ResponseEntity<Object> handleComputerNotFound(ComputerNotFoundException ex, WebRequest request) {
		  APIError error = new APIError()
				  .withMessage("Requested Computer not found")
				  .withStatus(HttpStatus.NOT_FOUND)
				  .withDetail("No computer with this Id could be found");
		return handleExceptionInternal(ex, error.toJson(), new HttpHeaders(), error.getStatus(), request);
	}
	  
	  @ExceptionHandler(value = { CompanyNotFoundException.class })
	  protected ResponseEntity<Object> handleConflict(CompanyNotFoundException ex, WebRequest request) {
		  APIError error = new APIError()
				  .withMessage("Requested Company not found")
				  .withStatus(HttpStatus.NOT_FOUND)
				  .withDetail("No company with this Id could be found");
		return handleExceptionInternal(ex, error.toJson(), new HttpHeaders(), error.getStatus(), request);
	}
	  
	  @Override
	  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		  APIError error = new APIError()
				  .withMessage(HttpStatus.BAD_REQUEST.toString())
				  .withStatus(HttpStatus.BAD_REQUEST)
				  .withDetail(ex.getMessage());
		return handleExceptionInternal(ex, error.toJson(), new HttpHeaders(), error.getStatus(), request);
	}

}
