package com.jifa.bankid.client;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.bankid.rpservice.v4_0_0.types.AuthenticateRequestType;
import com.bankid.rpservice.v4_0_0.types.CollectResponseType;
import com.bankid.rpservice.v4_0_0.types.ObjectFactory;
import com.bankid.rpservice.v4_0_0.types.OrderResponseType;
import com.bankid.rpservice.v4_0_0.types.ProgressStatusType;
import com.bankid.rpservice.v4_0_0.types.SignRequestType;
import com.jifa.bankid.model.AuthenticateRequest;
import com.jifa.bankid.model.AuthenticateResponse;
import com.jifa.bankid.model.CollectRequest;
import com.jifa.bankid.model.CollectResponse;
import com.jifa.bankid.model.ProgressStatus;
import com.jifa.bankid.model.SignRequest;
import com.jifa.bankid.model.SignResponse;
import com.jifa.bankid.model.UserInfo;

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

	public SignResponse sign(SignRequest signRequest) {
		SignRequestType request = objectFactory.createSignRequestType();
		request.setPersonalNumber(signRequest.getPersonalNumber());
		request.setUserVisibleData(Base64Utils.encodeToString(signRequest.getUserVisibleData().getBytes()));

		if (!StringUtils.isEmpty(signRequest.getUserNonVisibleData())) {
			request.setUserNonVisibleData(Base64Utils.encodeToString(signRequest.getUserNonVisibleData().getBytes()));
		}

		OrderResponseType orderResponseType = makeCall(OrderResponseType.class,
				objectFactory.createSignRequest(request));

		return new SignResponse(orderResponseType.getOrderRef(), orderResponseType.getAutoStartToken());

	}

}
