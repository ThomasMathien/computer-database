package com.excilys.computerDatabase.controller.restController;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class APIError {

	private String message;
	private HttpStatus status; 
	private String detail;
	
	public APIError() {
	}
	
	public APIError(String message, HttpStatus status, String detail) {
		this.message = message;
		this.status = status;
		this.detail = detail;
	}
	
	public APIError withStatus(HttpStatus status) {
		this.status = status;
		return this;
	}

	public APIError withMessage(String message) {
		this.message = message;
		return this;
	}
	public APIError withDetail(String detail) {
		this.detail = detail;
		return this;
	}
	
	public ObjectNode toJson() {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("message", message);
		node.put("status", status.value());
		node.put("detail", detail);
		return node;
	}
	
	public HttpStatus getStatus() {
		return this.status;
	}
}
