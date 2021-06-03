package com.java.assessment.clientmemberapp.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The ClientExceptionHandler will handle the Global and Custom exceptions.
 *
 * @author Sowjanya Gonuguntla
 */
@ControllerAdvice
public class ClientExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		return new ResponseEntity<Object>(prepareErrorResponse(ex, status), status);
	}

	@ExceptionHandler(ClientNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(ClientNotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<Object>(prepareErrorResponse(ex, status), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { DuplicateValuesException.class, ClientDataValidationException.class })
	public final ResponseEntity<Object> handleDuplicateInsertionException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<Object>(prepareErrorResponse(ex, status), HttpStatus.BAD_REQUEST);

	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		prepareErrorResponse(ex, status);
		return new ResponseEntity<Object>(prepareErrorResponse(ex, status), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	}

	private ErrorResponse prepareErrorResponse(Exception ex, HttpStatus status) {
		ErrorResponse exceptionResponse = new ErrorResponse(new Date(), status.value(), status.getReasonPhrase(),
				ex.getMessage());
		return exceptionResponse;
	}

}
