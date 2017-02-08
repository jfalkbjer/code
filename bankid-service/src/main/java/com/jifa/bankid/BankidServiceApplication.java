package com.jifa.bankid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.bankid.rpservice.v4_0_0.types.CollectResponseType;
import com.bankid.rpservice.v4_0_0.types.ProgressStatusType;

/**
 * https://www.bankid.com/bankid-i-dina-tjanster/rp-info
 * 
 * <pre>
 *   List the BankId cert:
 *   keytool -v -list -storetype pkcs12 -keystore FPTestcert2_20150818_102329.pfx
 *   
 *   Get the certificate:
 *   openssl pkcs12 -in FPTestcert2_20150818_102329.pfx -out FPTestcert2_20150818_102329.pem -nodes
 * 
 *   Keep everything between BEGIN CERTIFICATE and END CERTIFICATE including BEGIN
 *   and END lines, and then import to cacerts.
 *   keytool -keystore cacerts -importcert -alias bankid-ca -file FPTestcert2_20150818_102329.pem
 * </pre>
 * 
 */
@SpringBootApplication
public class BankidServiceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(BankidServiceApplication.class, args);

		BankIdClient bankIdClient = applicationContext.getBean(BankIdClient.class);

		try {
			AuthenticateResponse authenticateResponse = bankIdClient
					.authenticate(new AuthenticateRequest("197306258950"));

			CollectResponseType collectResponseType = bankIdClient.collect(authenticateResponse.getOrderRef());

			while (ProgressStatusType.COMPLETE != collectResponseType.getProgressStatus()) {
				try {
					Thread.sleep(5000);
					collectResponseType = bankIdClient.collect(authenticateResponse.getOrderRef());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println(collectResponseType.getUserInfo().getGivenName());
		} catch (SoapFaultClientException e) {
			System.out.println(e.getFaultCode().getLocalPart());
			System.out.println(e.getFaultStringOrReason());
		}
	}

}
