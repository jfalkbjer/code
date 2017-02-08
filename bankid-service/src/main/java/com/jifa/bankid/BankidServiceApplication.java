package com.jifa.bankid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

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
		AuthenticateResponse response = bankIdClient.authenticate(new AuthenticateRequest("197306258950"));

		System.out.println(response.getOrderRef());
	}

}
