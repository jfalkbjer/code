package com.jifa.bankid;

import com.bankid.rpservice.v4_0_0.types.AuthenticateRequestType;
import com.bankid.rpservice.v4_0_0.types.CollectResponseType;
import com.bankid.rpservice.v4_0_0.types.ObjectFactory;
import com.bankid.rpservice.v4_0_0.types.OrderResponseType;
import com.bankid.rpservice.v4_0_0.types.ProgressStatusType;

public class BankIdClient extends AbstractBankIdClient {

	private final ObjectFactory objectFactory = new ObjectFactory();

	public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest) {
		AuthenticateRequestType request = objectFactory.createAuthenticateRequestType();
		request.setPersonalNumber(authenticateRequest.getPersonalNumber());

		OrderResponseType orderResponseType = makeCall(OrderResponseType.class,
				objectFactory.createAuthenticateRequest(request));

		return new AuthenticateResponse(orderResponseType.getOrderRef(), orderResponseType.getAutoStartToken());
	}

	public CollectResponse collect(CollectRequest collectRequest) {
		CollectResponseType collectResponseType = makeCall(CollectResponseType.class,
				objectFactory.createOrderRef(collectRequest.getOrderRef()));

		UserInfo userInfo = null;

		if (collectResponseType.getProgressStatus() == ProgressStatusType.COMPLETE) {
			userInfo = new UserInfo(collectResponseType.getUserInfo().getName(),
					collectResponseType.getUserInfo().getGivenName(), collectResponseType.getUserInfo().getSurname(),
					collectResponseType.getUserInfo().getPersonalNumber());
		}

		return new CollectResponse(ProgressStatus.valueOf(collectResponseType.getProgressStatus().name()),
				collectResponseType.getSignature(), userInfo);
	}

}
