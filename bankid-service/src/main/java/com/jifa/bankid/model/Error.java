package com.jifa.bankid.model;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class Error {

	private final String traceId;
	private final int code;
	private final String message;

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
