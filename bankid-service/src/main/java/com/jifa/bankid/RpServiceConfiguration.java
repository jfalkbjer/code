package com.jifa.bankid;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *     http://zoltanaltfatter.com/2016/04/30/soap-over-https-with-client-certificate-authentication/
 * 
 *     http://www.robinhowlett.com/blog/2016/01/05/everything-you-ever-wanted-to-know-about-ssl-but-were-afraid-to-ask/
 *     https://github.com/robinhowlett/everything-ssl
 * </pre>
 */
@Slf4j
@Configuration
public class RpServiceConfiguration {

	private static final String BANKID_PACKAGE = "com.bankid.rpservice.v4_0_0.types";

	@Autowired
	BankIdProperties bankIdProperties;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPaths(BANKID_PACKAGE);

		return marshaller;
	}

	@Bean
	public BankIdClient bankIdClient(Jaxb2Marshaller marshaller) throws Exception {
		// Needed to be able to present client certificate for Server.
		KeyManagerFactory keyManagerFactory = createKeyManagerFactory();

		// Needed to verify/trust the server host.
		TrustManagerFactory trustManagerFactory = createTrustManagerFactory();

		HttpsUrlConnectionMessageSender messageSender = new HttpsUrlConnectionMessageSender();
		messageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
		messageSender.setTrustManagers(trustManagerFactory.getTrustManagers());

		BankIdClient bankIdClient = new BankIdClient();
		bankIdClient.setDefaultUri(bankIdProperties.getUrl());
		bankIdClient.setMarshaller(marshaller);
		bankIdClient.setUnmarshaller(marshaller);
		bankIdClient.setMessageSender(messageSender);

		return bankIdClient;
	}

	private TrustManagerFactory createTrustManagerFactory()
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore keystore = createKeyStore(bankIdProperties.getTrustStore(), "JKS",
				bankIdProperties.getTrustStorePassword());
		log.info("Loaded truststore: {0}", bankIdProperties.getTrustStore().getURI().toString());

		TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		factory.init(keystore);

		return factory;
	}

	private KeyManagerFactory createKeyManagerFactory() throws KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException, UnrecoverableKeyException {
		KeyStore keystore = createKeyStore(bankIdProperties.getKeyStore(), "PKCS12",
				bankIdProperties.getKeyStorePassword());
		log.info("Loaded keystore: {0}", bankIdProperties.getKeyStore().getURI().toString());

		KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		factory.init(keystore, bankIdProperties.getKeyStorePassword().toCharArray());

		return factory;
	}

	private KeyStore createKeyStore(Resource resource, String keyStoreType, String password)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore keystore = KeyStore.getInstance(keyStoreType);
		keystore.load(resource.getInputStream(), password.toCharArray());
		IOUtils.closeQuietly(resource.getInputStream());

		return keystore;
	}

}
