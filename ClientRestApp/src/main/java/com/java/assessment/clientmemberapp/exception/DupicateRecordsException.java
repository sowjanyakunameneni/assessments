package com.java.assessment.clientmemberapp.exception;

/**
 * The Duplicate  client records Exception
 *
 * @author Sowjanya Gonuguntla
 */
public class DupicateRecordsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DupicateRecordsException(String message) {
		super(message);

	}

}
