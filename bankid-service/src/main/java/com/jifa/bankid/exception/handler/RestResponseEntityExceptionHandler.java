package com.jifa.bankid.exception.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.jifa.bankid.model.Error;
import com.jifa.bankid.model.ValidationError;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private SpanAccessor spanAccessor;

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> handleException(RuntimeException exception, WebRequest webRequest) {
		String traceId = Span.idToHex(spanAccessor.getCurrentSpan().getTraceId());
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		Error error = new Error(traceId, httpStatus.value(), exception.getMessage(), null);

		return handleExceptionInternal(exception, error, new HttpHeaders(), httpStatus, webRequest);
	}

	@ExceptionHandler(value = { SoapFaultClientException.class })
	protected ResponseEntity<Object> handleSoapFaultClientException(RuntimeException exception, WebRequest webRequest) {
		String traceId = Span.idToHex(spanAccessor.getCurrentSpan().getTraceId());

		// We treat all as INTERNAL_SERVER_ERROR, because BankId always seem to
		// return <faultcode>soap:Server</faultcode>, no matter what.
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		Error error = new Error(traceId, httpStatus.value(),
				((SoapFaultClientException) exception).getFaultStringOrReason(), null);

		return handleExceptionInternal(exception, error, new HttpHeaders(), httpStatus, webRequest);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String traceId = Span.idToHex(spanAccessor.getCurrentSpan().getTraceId());

		List<ValidationError> validationErrors = processFieldErrors(ex.getBindingResult().getFieldErrors());

		// Send null as message, because the exception gives a very detailed
		// message and we only need to tell the field/s that did not validate.
		Error error = new Error(traceId, status.value(), null, validationErrors);

		return handleExceptionInternal(ex, error, headers, status, request);
	}

	private List<ValidationError> processFieldErrors(List<FieldError> fieldErrors) {
		List<ValidationError> validationErrors = new ArrayList<>();

		fieldErrors.forEach(fieldError -> validationErrors
				.add(new ValidationError(fieldError.getField(), fieldError.getDefaultMessage())));

		return validationErrors;
	}

}
