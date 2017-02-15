package com.jifa.bankid.model;

import lombok.Data;

@Data
public class SignResponse {

	private final String orderRef;
	private final String autoStartToken;

}
