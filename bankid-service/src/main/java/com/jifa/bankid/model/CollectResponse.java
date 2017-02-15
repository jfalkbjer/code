package com.jifa.bankid.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CollectResponse {

	private final ProgressStatus progressStatus;
	private final String signature;
	private final UserInfo userInfo;

}
