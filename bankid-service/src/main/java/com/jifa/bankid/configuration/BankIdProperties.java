package com.jifa.bankid.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "bankid.ws")
@Component
@Data
public class BankIdProperties {

	private String url;
	private Resource keyStore;
	private String keyStorePassword;
	private Resource trustStore;
	private String trustStorePassword;

}
