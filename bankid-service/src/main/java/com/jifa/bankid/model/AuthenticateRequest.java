package com.jifa.bankid.model;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Data
public class AuthenticateRequest {

	@NotNull
	private final String personalNumber;
}
