package com.jifa.bankid;

import com.bankid.rpservice.v4_0_0.types.AuthenticateRequestType;
import com.bankid.rpservice.v4_0_0.types.CollectResponseType;
import com.bankid.rpservice.v4_0_0.types.ObjectFactory;
import com.bankid.rpservice.v4_0_0.types.OrderResponseType;

public class BankIdClient extends AbstractBankIdClient {

	private final ObjectFactory objectFactory = new ObjectFactory();

	public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest) {
		AuthenticateRequestType request = objectFactory.createAuthenticateRequestType();
		request.setPersonalNumber(authenticateRequest.getPersonalNumber());

		OrderResponseType orderResponseType = makeCall(OrderResponseType.class,
				objectFactory.createAuthenticateRequest(request));

		return new AuthenticateResponse(orderResponseType.getOrderRef());
	}

	public CollectResponseType collect(String orderRef) {
		CollectResponseType collectResponseType = makeCall(CollectResponseType.class,
				objectFactory.createOrderRef(orderRef));

		return collectResponseType;
	}

}
