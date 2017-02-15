package com.jifa.bankid.model;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Error {

	private final String traceId;
	private final int code;

	// Will not be sent if we have validation errors.
	private final String message;

	// Will not be sent if not returning any validation errors.
	private final List<ValidationError> validationErrors;

	/**
	 * Get time when error response is sent back to client in ISO_INSTANT
	 * format, e.g. 2017-02-01T18:14:07.072Z
	 * 
	 * @return {@link String} with ISO_INSTANT formatted representation of now.
	 */
	public String getTime() {
		return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
	}

}
