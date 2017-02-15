package com.jifa.bankid.model;

import lombok.Data;

@Data
public class ValidationError {

	private final String field;
	private final String message;

}
