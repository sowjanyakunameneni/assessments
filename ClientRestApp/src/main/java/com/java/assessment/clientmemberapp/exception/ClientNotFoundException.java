package com.java.assessment.clientmemberapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Client Not Found Exception
 *
 * @author Sowjanya Gonuguntla
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientNotFoundException(String message) {
		super(message);
	}

}
