package com.jifa.bankid.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Data
public class SignRequest {

	@NotNull
	@Pattern(regexp = "\\d{10,12}")
	private final String personalNumber;

	@NotNull
	private final String userVisibleData;

	private final String userNonVisibleData;

}
