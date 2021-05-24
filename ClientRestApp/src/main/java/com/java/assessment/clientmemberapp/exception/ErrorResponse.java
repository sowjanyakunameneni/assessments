package com.java.assessment.clientmemberapp.exception;

import java.util.Date;

/**
 * The Error Response
 *
 * @author Sowjanya Gonuguntla
 */
public class ErrorResponse {

	private Date timestamp;
	private int statusCode;
	private String error;
	private String message;

	public ErrorResponse(Date timestamp, int statusCode, String error, String message) {
		super();
		this.timestamp = timestamp;
		this.statusCode = statusCode;
		this.error = error;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ErrorResponse [timestamp=" + timestamp + ", statusCode=" + statusCode + ", error=" + error
				+ ", message=" + message + "]";
	}

}
