package com.jifa.bankid.client;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public abstract class AbstractBankIdClient extends WebServiceGatewaySupport {

	@SuppressWarnings("unchecked")
	public <T> T makeCall(Class<T> responseClass, Object request) {
		return ((JAXBElement<T>) getWebServiceTemplate().marshalSendAndReceive(request)).getValue();
	}

}
