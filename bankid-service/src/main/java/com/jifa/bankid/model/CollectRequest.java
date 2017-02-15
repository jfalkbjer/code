package com.jifa.bankid.model;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Data
public class CollectRequest {

	@NotNull
	private final String orderRef;
}
