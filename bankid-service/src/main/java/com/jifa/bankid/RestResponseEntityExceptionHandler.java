package com.jifa.bankid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.ws.soap.client.SoapFaultClientException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private SpanAccessor spanAccessor;

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> handleException(RuntimeException exception, WebRequest webRequest) {
		String traceId = Span.idToHex(spanAccessor.getCurrentSpan().getTraceId());
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		Error error = new Error(traceId, httpStatus.value(), exception.getMessage());

		return handleExceptionInternal(exception, error, new HttpHeaders(), httpStatus, webRequest);
	}

	@ExceptionHandler(value = { SoapFaultClientException.class })
	protected ResponseEntity<Object> handleSoapFaultClientException(RuntimeException exception, WebRequest webRequest) {
		String traceId = Span.idToHex(spanAccessor.getCurrentSpan().getTraceId());

		// We treat all as INTERNAL_SERVER_ERROR, because BankId always seem to
		// return <faultcode>soap:Server</faultcode>, no matter what.
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		Error error = new Error(traceId, httpStatus.value(),
				((SoapFaultClientException) exception).getFaultStringOrReason());

		return handleExceptionInternal(exception, error, new HttpHeaders(), httpStatus, webRequest);
	}

}
