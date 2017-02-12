package com.jifa.bankid;

import lombok.Data;

@Data
public class AuthenticateResponse {

	private final String orderRef;
	private final String autoStartToken;

}
