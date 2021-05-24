package com.java.assessment.clientmemberapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The ClientDataValidationException to handle the Client data validation
 * exceptions.
 *
 * @author Sowjanya Gonuguntla
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientDataValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClientDataValidationException(String message) {
		super(message);
	}

}
