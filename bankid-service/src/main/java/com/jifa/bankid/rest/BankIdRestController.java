package com.jifa.bankid.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jifa.bankid.client.BankIdClient;
import com.jifa.bankid.model.AuthenticateRequest;
import com.jifa.bankid.model.AuthenticateResponse;
import com.jifa.bankid.model.CollectRequest;
import com.jifa.bankid.model.CollectResponse;
import com.jifa.bankid.model.SignRequest;
import com.jifa.bankid.model.SignResponse;

@RestController
public class BankIdRestController {

	@Autowired
	BankIdClient bankIdClient;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/authenticate")
	public AuthenticateResponse authenticate(@RequestBody @Valid AuthenticateRequest authenticateRequest) {
		return bankIdClient.authenticate(authenticateRequest);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/collect")
	public CollectResponse authenticate(@RequestBody @Valid CollectRequest collectRequest) {
		return bankIdClient.collect(collectRequest);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/sign")
	public SignResponse sign(@RequestBody @Valid SignRequest signRequest) {
		return bankIdClient.sign(signRequest);
	}

}
